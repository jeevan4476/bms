package com.bms.controller;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bms.dto.BookingRequest;
import com.bms.dto.BookingResponse;
import com.bms.entity.Seat;
import com.bms.entity.Show;
import com.bms.entity.User;
import com.bms.entity_enums.VenueLayoutType;
import com.bms.repository.BookingSeatRepository;
import com.bms.repository.SeatRepository;
import com.bms.repository.UserRepository;
import com.bms.service.BookingService;
import com.bms.service.SeatLockService;
import com.bms.service.ShowService;

@Controller
public class PageShowController {

    private final ShowService showService;
    private final SeatRepository seatRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final BookingService bookingService;
    private final UserRepository userRepository;
    private final SeatLockService seatLockService;

    public PageShowController(
            ShowService showService,
            SeatRepository seatRepository,
            BookingSeatRepository bookingSeatRepository,
            BookingService bookingService,
            UserRepository userRepository,
            SeatLockService seatLockService) {
        this.showService = showService;
        this.seatRepository = seatRepository;
        this.bookingSeatRepository = bookingSeatRepository;
        this.bookingService = bookingService;
        this.userRepository = userRepository;
        this.seatLockService = seatLockService;
    }

    @GetMapping("/shows/{id}")
    public String showDetail(
            @PathVariable Long id,
            @RequestParam(value = "section", required = false) String selectedSection,
            Model model,
            Authentication authentication,
            @RequestHeader(value = "HX-Request", required = false) boolean htmxRequest) {

        Show show = showService.getShow(id);
        VenueLayoutType layoutType = show.getVenue().getLayoutType() != null
                ? show.getVenue().getLayoutType() : VenueLayoutType.THEATRE;
        String currentUserId = (authentication != null) ? authentication.getName() : null;

        List<Seat> allSeats = seatRepository.findByVenueId(show.getVenue().getId());

        // Booked seat IDs for this show
        Set<Long> bookedSeatIds = bookingSeatRepository
                .findByShowId(id)
                .stream()
                .map(bs -> bs.getSeat().getId())
                .collect(Collectors.toSet());

        // Distinct ordered sections
        List<String> sections = allSeats.stream()
                .map(Seat::getSectionName)
                .filter(s -> s != null && !s.isBlank())
                .distinct()
                .collect(Collectors.toList());

        // Determine active section (default: first for stadium/arena)
        String activeSection = selectedSection;
        if (activeSection == null && !sections.isEmpty()
                && layoutType != VenueLayoutType.THEATRE) {
            activeSection = sections.get(0);
        }

        // Filter seats for seat grid
        List<Seat> displaySeats = allSeats;
        if (activeSection != null && layoutType != VenueLayoutType.THEATRE) {
            final String sectionFilter = activeSection;
            displaySeats = allSeats.stream()
                    .filter(s -> sectionFilter.equals(s.getSectionName()))
                    .collect(Collectors.toList());
        }

        // Build seat map (row -> list of seatInfo)
        Map<String, List<Map<String, Object>>> seatMap = new LinkedHashMap<>();
        for (Seat seat : displaySeats) {
            String row = seat.getSeatRow();
            seatMap.computeIfAbsent(row, k -> new ArrayList<>());

            Map<String, Object> seatInfo = new LinkedHashMap<>();
            seatInfo.put("id", seat.getId());
            seatInfo.put("number", seat.getSeatNumber());
            seatInfo.put("row", row);
            seatInfo.put("section", seat.getSectionName());
            seatInfo.put("seatType", seat.getSeatType());

            boolean booked = bookedSeatIds.contains(seat.getId());
            seatInfo.put("booked", booked);

            if (!booked) {
                String lockOwner = seatLockService.getLockOwner(id, seat.getId());
                if (lockOwner != null) {
                    seatInfo.put("status", lockOwner.equals(currentUserId) ? "LOCKED_BY_ME" : "LOCKED_BY_OTHER");
                } else {
                    seatInfo.put("status", "AVAILABLE");
                }
            } else {
                seatInfo.put("status", "BOOKED");
            }

            seatMap.get(row).add(seatInfo);
        }

        model.addAttribute("show", show);
        model.addAttribute("seatMap", seatMap);
        model.addAttribute("bookedSeatIds", bookedSeatIds);
        model.addAttribute("layoutType", layoutType.name());
        model.addAttribute("sections", sections);
        model.addAttribute("activeSection", activeSection);

        if (htmxRequest) {
            return "show-detail :: seatMapFragment";
        }
        return "show-detail";
    }

    @PostMapping("/shows/{id}/book")
    public String bookSeats(
            @PathVariable Long id,
            @RequestParam("seatIds") List<Long> seatIds,
            Authentication authentication,
            RedirectAttributes redirectAttributes) {

        if (authentication == null || !authentication.isAuthenticated()) {
            redirectAttributes.addFlashAttribute("error", "Please log in to book seats.");
            return "redirect:/login";
        }

        try {
            String email = authentication.getName();
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            BookingRequest request = new BookingRequest();
            request.setUserId(user.getId());
            request.setShowId(id);
            request.setSeatIds(seatIds);

            BookingResponse response = bookingService.createBooking(request);

            redirectAttributes.addFlashAttribute("success",
                    "Booking created successfully! Booking #" + response.getBookingId());
            return "redirect:/bookings/" + response.getBookingId();

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Booking failed: " + e.getMessage());
            return "redirect:/shows/" + id;
        }
    }
}
