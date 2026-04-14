package com.bms.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShowPerformanceReport {
    private Long showId;
    private String eventTitle;
    private String venueName;
    private Long totalSeats;
    private Long bookedSeats;
    private double occupancyPercent;
    private double revenue;
    private double revenuePerSeat; // Revenue / Total Seats
}
