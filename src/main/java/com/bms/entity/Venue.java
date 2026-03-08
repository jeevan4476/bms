package com.bms.entity;

import jakarta.persistence.Entity;

@Entity
public class Venue extends BaseEntity {

    private String name;

    private String location;

    private Integer totalCapacity;
}