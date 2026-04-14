package com.bms.entity;

import com.bms.entity_enums.VenueLayoutType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Venue extends BaseEntity {

    private String name;

    private String location;

    private Integer totalCapacity;

    @Enumerated(EnumType.STRING)
    private VenueLayoutType layoutType;
}