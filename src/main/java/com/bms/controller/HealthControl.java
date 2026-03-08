package com.bms.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthControl {
    @GetMapping("/")
    public String healthCheck() {
        return "OK";
    }
}
