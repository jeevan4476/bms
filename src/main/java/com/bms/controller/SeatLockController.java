package com.bms.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import com.bms.service.SeatLockService;

import java.util.Map;

/**
 * Controller for real-time seat locking functionality.
 * Interacts with Redis to provide instant locking/unlocking as seats are clicked.
 */
@RestController
@RequestMapping("/api/locks")
public class SeatLockController {

    private static final Logger log = LoggerFactory.getLogger(SeatLockController.class);
    private final SeatLockService seatLockService;

    public SeatLockController(SeatLockService seatLockService) {
        this.seatLockService = seatLockService;
    }

    @PostMapping("/shows/{showId}/seats/{seatId}/toggle")
    public ResponseEntity<?> toggleLock(
            @PathVariable Long showId,
            @PathVariable Long seatId,
            Authentication authentication) {

        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).body("Please login to select seats.");
        }

        String userId = authentication.getName(); // Using email as lock owner ID
        String owner = seatLockService.getLockOwner(showId, seatId);

        if (owner == null) {
            // Unlocked, try to lock
            boolean locked = seatLockService.lockSeat(showId, seatId, userId);
            if (locked) {
                log.info("Seat {} locked by {} for show {}", seatId, userId, showId);
                return ResponseEntity.ok(Map.of("status", "LOCKED_BY_ME"));
            } else {
                return ResponseEntity.status(409).body("Seat was just locked by someone else.");
            }
        } else if (owner.equals(userId)) {
            // Locked by current user, unlock it
            seatLockService.releaseSeat(showId, seatId);
            log.info("Seat {} unlocked by {} for show {}", seatId, userId, showId);
            return ResponseEntity.ok(Map.of("status", "AVAILABLE"));
        } else {
            // Locked by someone else
            return ResponseEntity.status(409).body("Seat is currently selected by another user.");
        }
    }
}
