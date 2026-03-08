package com.bms.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class BookingSeat extends BaseEntity {

    @ManyToOne
    private Booking booking;

    @ManyToOne
    private Seat seat;

    private Double price;
}