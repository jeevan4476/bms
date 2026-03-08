package com.bms.entity;

import com.bms.entity_enums.EventType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Entity
public class Event extends BaseEntity {

    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    private EventType eventType;

    private Integer durationMinutes;

    private String imageUrl;
}