package com.bms.entity;

import java.time.LocalDateTime;

import com.bms.entity_enums.BookingStatus;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;

@Entity
public class Booking extends BaseEntity {

    @ManyToOne
    private User user;

    @ManyToOne
    private Show show;

    private LocalDateTime bookingTime;

    @Enumerated(EnumType.STRING)
    private BookingStatus status;

    private Double totalAmount;
}