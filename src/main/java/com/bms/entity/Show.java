package com.bms.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;

@Entity
public class Show extends BaseEntity {

    @ManyToOne
    private Event event;

    @ManyToOne
    private Venue venue;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private Double price;
}