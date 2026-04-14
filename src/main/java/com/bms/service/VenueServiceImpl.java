package com.bms.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.bms.dto.VenueRequest;
import com.bms.dto.VenueRequest.SectionRequest;
import com.bms.entity.Seat;
import com.bms.entity.Venue;
import com.bms.entity_enums.SeatType;
import com.bms.entity_enums.VenueLayoutType;
import com.bms.exception.ResourceNotFoundException;
import com.bms.repository.SeatRepository;
import com.bms.repository.VenueRepository;

import java.util.List;

@Service
public class VenueServiceImpl implements VenueService {

    private static final Logger log = LoggerFactory.getLogger(VenueServiceImpl.class);

    private final VenueRepository venueRepository;
    private final SeatRepository seatRepository;

    public VenueServiceImpl(VenueRepository venueRepository, SeatRepository seatRepository) {
        this.venueRepository = venueRepository;
        this.seatRepository = seatRepository;
    }

    @Override
    public Venue createVenue(VenueRequest request) {
        Venue venue = new Venue();
        venue.setName(request.getName());
        venue.setLocation(request.getLocation());

        VenueLayoutType layout = request.getLayoutType() != null
                ? request.getLayoutType() : VenueLayoutType.THEATRE;
        venue.setLayoutType(layout);
        venue = venueRepository.save(venue);

        if (request.getSections() != null && !request.getSections().isEmpty()) {
            generateSectionedSeats(venue, request.getSections());
        } else if (request.getRows() != null && request.getSeatsPerRow() != null) {
            generateSeats(venue, request.getRows(), request.getSeatsPerRow(), null, SeatType.REGULAR);
        }

        log.info("Venue created: id={}, name={}, layout={}", venue.getId(), venue.getName(), layout);
        return venue;
    }

    private void generateSectionedSeats(Venue venue, List<SectionRequest> sections) {
        for (SectionRequest section : sections) {
            generateSeats(venue, section.getRows(), section.getSeatsPerRow(),
                    section.getName(),
                    section.getSeatType() != null ? section.getSeatType() : SeatType.REGULAR);
        }
    }

    private void generateSeats(Venue venue, int rows, int seatsPerRow, String sectionName, SeatType type) {
        for (int i = 0; i < rows; i++) {
            String rowLabel = sectionName != null
                    ? sectionName.substring(0, Math.min(2, sectionName.length())).toUpperCase() + (i + 1)
                    : String.valueOf((char) ('A' + i));

            for (int j = 1; j <= seatsPerRow; j++) {
                Seat seat = new Seat();
                seat.setSeatRow(rowLabel);
                seat.setSeatNumber(j);
                seat.setSeatType(type);
                seat.setSectionName(sectionName);
                seat.setVenue(venue);
                seatRepository.save(seat);
            }
        }
    }

    @Override
    public Venue getVenue(Long id) {
        return venueRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Venue not found with id " + id));
    }
}
