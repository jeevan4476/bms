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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.bms.dto.BookingRequest;
import com.bms.dto.BookingResponse;
import com.bms.entity.Seat;
import com.bms.entity.Show;
import com.bms.entity.User;
import com.bms.repository.BookingSeatRepository;
import com.bms.repository.SeatRepository;
import com.bms.repository.UserRepository;
import com.bms.service.BookingService;
import com.bms.service.ShowService;

@Controller
public class PageShowController {

    private final ShowService showService;
    private final SeatRepository seatRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final BookingService bookingService;
    private final UserRepository userRepository;
    private final com.bms.service.SeatLockService seatLockService;

    public PageShowController(
            ShowService showService,
            SeatRepository seatRepository,
            BookingSeatRepository bookingSeatRepository,
            BookingService bookingService,
            UserRepository userRepository,
            com.bms.service.SeatLockService seatLockService) {
        this.showService = showService;
        this.seatRepository = seatRepository;
        this.bookingSeatRepository = bookingSeatRepository;
        this.bookingService = bookingService;
        this.userRepository = userRepository;
        this.seatLockService = seatLockService;
    }

    @GetMapping("/shows/{id}")
    public String showDetail(@PathVariable Long id, 
                            Model model, 
                            Authentication authentication,
                            @org.springframework.web.bind.annotation.RequestHeader(value = "HX-Request", required = false) boolean htmxRequest) {
        
        Show show = showService.getShow(id);
        List<Seat> allSeats = seatRepository.findByVenueId(show.getVenue().getId());
        String currentUserId = (authentication != null) ? authentication.getName() : null;

        // Find which seats are already booked for this show (read-only, no lock)
        Set<Long> bookedSeatIds = bookingSeatRepository
                .findByShowId(id)
                .stream()
                .map(bs -> bs.getSeat().getId())
                .collect(Collectors.toSet());

        // Organize seats by row for the seat map
        Map<String, List<Map<String, Object>>> seatMap = new LinkedHashMap<>();
        for (Seat seat : allSeats) {
            String row = seat.getSeatRow();
            seatMap.computeIfAbsent(row, k -> new ArrayList<>());

            Map<String, Object> seatInfo = new LinkedHashMap<>();
            seatInfo.put("id", seat.getId());
            seatInfo.put("number", seat.getSeatNumber());
            seatInfo.put("row", row);
            
            boolean booked = bookedSeatIds.contains(seat.getId());
            seatInfo.put("booked", booked);

            // Redis Lock Status
            if (!booked) {
                String lockOwner = seatLockService.getLockOwner(id, seat.getId());
                if (lockOwner != null) {
                    if (lockOwner.equals(currentUserId)) {
                        seatInfo.put("status", "LOCKED_BY_ME");
                    } else {
                        seatInfo.put("status", "LOCKED_BY_OTHER");
                    }
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
            // Get user by email from authentication
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
            redirectAttributes.addFlashAttribute("error",
                    "Booking failed: " + e.getMessage());
            return "redirect:/shows/" + id;
        }
    }
}
