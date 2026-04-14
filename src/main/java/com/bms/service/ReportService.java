package com.bms.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bms.dto.EventBookingReport;
import com.bms.dto.PopularEventReport;
import com.bms.dto.RevenueReport;
import com.bms.dto.SeatOccupancyReport;
import com.bms.entity.Show;
import com.bms.repository.BookingRepository;
import com.bms.repository.BookingSeatRepository;
import com.bms.repository.SeatRepository;
import com.bms.repository.ShowRepository;

@Service
public class ReportService {

    private static final Logger log = LoggerFactory.getLogger(ReportService.class);

    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final SeatRepository seatRepository;
    private final ShowRepository showRepository;

    public ReportService(BookingRepository bookingRepository,
                         BookingSeatRepository bookingSeatRepository,
                         SeatRepository seatRepository,
                         ShowRepository showRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingSeatRepository = bookingSeatRepository;
        this.seatRepository = seatRepository;
        this.showRepository = showRepository;
    }

    /** Total confirmed bookings count */
    public long getTotalBookings() {
        long count = bookingRepository.countConfirmedBookings();
        log.info("Total confirmed bookings: {}", count);
        return count;
    }

    /** Total revenue from confirmed bookings */
    public double getTotalRevenue() {
        double revenue = bookingRepository.sumConfirmedRevenue();
        log.info("Total confirmed revenue: {}", revenue);
        return revenue;
    }

    /** Bookings grouped by event */
    public List<EventBookingReport> getBookingsPerEvent() {
        log.info("Fetching bookings per event report");
        List<Object[]> rows = bookingRepository.countBookingsPerEvent();
        List<EventBookingReport> reports = new ArrayList<>();
        for (Object[] row : rows) {
            reports.add(new EventBookingReport(
                    (Long) row[0],
                    (String) row[1],
                    (Long) row[2]
            ));
        }
        return reports;
    }

    /** Revenue grouped by event */
    public List<RevenueReport> getRevenuePerEvent() {
        log.info("Fetching revenue per event report");
        List<Object[]> rows = bookingRepository.sumRevenuePerEvent();
        List<RevenueReport> reports = new ArrayList<>();
        for (Object[] row : rows) {
            reports.add(new RevenueReport(
                    (Long) row[0],
                    (String) row[1],
                    (Double) row[2]
            ));
        }
        return reports;
    }

    /** Most popular events by seats booked */
    public List<PopularEventReport> getPopularEvents() {
        log.info("Fetching popular events report");
        List<Object[]> rows = bookingSeatRepository.countSeatsBookedPerEvent();
        List<PopularEventReport> reports = new ArrayList<>();
        for (Object[] row : rows) {
            reports.add(new PopularEventReport(
                    (Long) row[0],
                    (String) row[1],
                    (Long) row[2]
            ));
        }
        return reports;
    }

    /** Seat occupancy per show */
    public List<SeatOccupancyReport> getSeatOccupancy() {
        log.info("Fetching seat occupancy report");
        List<Show> shows = showRepository.findAllWithEventAndVenue();
        List<SeatOccupancyReport> reports = new ArrayList<>();

        for (Show show : shows) {
            long totalSeats = seatRepository.countByVenueId(show.getVenue().getId());
            long bookedSeats = bookingSeatRepository.countByShowId(show.getId());

            double occupancy = totalSeats > 0
                    ? Math.round((double) bookedSeats / totalSeats * 10000.0) / 100.0
                    : 0.0;

            reports.add(new SeatOccupancyReport(
                    show.getId(),
                    show.getEvent().getTitle(),
                    show.getVenue().getName(),
                    totalSeats,
                    bookedSeats,
                    occupancy
            ));
        }

        return reports;
    }

    /** Detailed performance per show (Admin - All Shows) */
    public List<com.bms.dto.ShowPerformanceReport> getShowPerformance() {
        log.info("Fetching all show performance reports");
        List<Show> shows = showRepository.findAllWithEventAndVenue();
        List<Object[]> revenueRows = bookingRepository.sumRevenuePerShow();
        
        java.util.Map<Long, Double> revenueMap = new java.util.HashMap<>();
        for (Object[] row : revenueRows) {
            revenueMap.put((Long) row[0], (Double) row[1]);
        }

        List<com.bms.dto.ShowPerformanceReport> reports = new ArrayList<>();
        for (Show show : shows) {
            long totalSeats = seatRepository.countByVenueId(show.getVenue().getId());
            long bookedSeats = bookingSeatRepository.countByShowId(show.getId());
            double revenue = revenueMap.getOrDefault(show.getId(), 0.0);

            double occupancy = totalSeats > 0
                    ? Math.round((double) bookedSeats / totalSeats * 10000.0) / 100.0
                    : 0.0;
            
            double revPerSeat = totalSeats > 0 ? revenue / totalSeats : 0.0;

            reports.add(new com.bms.dto.ShowPerformanceReport(
                    show.getId(),
                    show.getEvent().getTitle(),
                    show.getVenue().getName(),
                    totalSeats,
                    bookedSeats,
                    occupancy,
                    revenue,
                    revPerSeat
            ));
        }
        return reports;
    }

    /** Detailed performance per show for a specific organiser */
    public List<com.bms.dto.ShowPerformanceReport> getShowPerformanceForOrganizer(Long organizerId) {
        log.info("Fetching show performance report for organizer: {}", organizerId);
        List<Show> shows = showRepository.findAllWithEventAndVenue();
        // Filter shows where event organizer matches
        List<Show> organizerShows = shows.stream()
                .filter(s -> s.getEvent().getOrganizer() != null && s.getEvent().getOrganizer().getId().equals(organizerId))
                .toList();
        
        List<Object[]> revenueRows = bookingRepository.sumRevenuePerShow();
        java.util.Map<Long, Double> revenueMap = new java.util.HashMap<>();
        for (Object[] row : revenueRows) {
            revenueMap.put((Long) row[0], (Double) row[1]);
        }

        List<com.bms.dto.ShowPerformanceReport> reports = new ArrayList<>();
        for (Show show : organizerShows) {
            long totalSeats = seatRepository.countByVenueId(show.getVenue().getId());
            long bookedSeats = bookingSeatRepository.countByShowId(show.getId());
            double revenue = revenueMap.getOrDefault(show.getId(), 0.0);

            double occupancy = totalSeats > 0
                    ? Math.round((double) bookedSeats / totalSeats * 10000.0) / 100.0
                    : 0.0;
            
            double revPerSeat = totalSeats > 0 ? revenue / totalSeats : 0.0;

            reports.add(new com.bms.dto.ShowPerformanceReport(
                    show.getId(),
                    show.getEvent().getTitle(),
                    show.getVenue().getName(),
                    totalSeats,
                    bookedSeats,
                    occupancy,
                    revenue,
                    revPerSeat
            ));
        }
        return reports;
    }
}
