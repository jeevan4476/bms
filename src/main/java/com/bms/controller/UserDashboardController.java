package com.bms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.bms.service.PaymentService;

/**
 * UserDashboardController handles user-facing account mappings.
 * Features here are authored by jeevan-cs240 (Payment & Transaction Engine).
 */
@Controller
public class UserDashboardController {

    private final PaymentService paymentService;

    public UserDashboardController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping("/my-bookings")
    public String myBookingsPage(Model model) {
        // Hardcoded userId for demo purposes in this phase
        model.addAttribute("transactions", paymentService.getTransactionHistory(1L));
        return "bookings";
    }
}
