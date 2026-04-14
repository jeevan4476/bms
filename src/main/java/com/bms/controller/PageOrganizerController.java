package com.bms.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.bms.entity.User;
import com.bms.repository.UserRepository;
import com.bms.service.ReportService;
import com.bms.dto.ShowPerformanceReport;

@Controller
@RequestMapping("/organizer")
public class PageOrganizerController {

    private final ReportService reportService;
    private final UserRepository userRepository;

    public PageOrganizerController(ReportService reportService, UserRepository userRepository) {
        this.reportService = reportService;
        this.userRepository = userRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, Authentication authentication) {
        String email = authentication.getName();
        User organizer = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Logged in user not found"));

        List<ShowPerformanceReport> reports = reportService.getShowPerformanceForOrganizer(organizer.getId());
        
        double totalRevenue = reports.stream().mapToDouble(ShowPerformanceReport::getRevenue).sum();
        long totalTickets = reports.stream().mapToLong(ShowPerformanceReport::getBookedSeats).sum();
        
        model.addAttribute("reports", reports);
        model.addAttribute("totalRevenue", totalRevenue);
        model.addAttribute("totalTickets", totalTickets);
        model.addAttribute("organizerName", organizer.getName());

        return "organizer/dashboard";
    }
}
