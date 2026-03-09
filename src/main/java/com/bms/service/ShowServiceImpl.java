package com.bms.service;

import org.springframework.stereotype.Service;

import com.bms.dto.ShowRequest;
import com.bms.entity.Event;
import com.bms.entity.Show;
import com.bms.entity.Venue;
import com.bms.exception.ResourceNotFoundException;
import com.bms.repository.EventRepository;
import com.bms.repository.ShowRepository;
import com.bms.repository.VenueRepository;

@Service
public class ShowServiceImpl implements ShowService {

    private final ShowRepository showRepository;
    private final EventRepository eventRepository;
    private final VenueRepository venueRepository;

    public ShowServiceImpl(ShowRepository showRepository,
                           EventRepository eventRepository,
                           VenueRepository venueRepository) {
        this.showRepository = showRepository;
        this.eventRepository = eventRepository;
        this.venueRepository = venueRepository;
    }

    @Override
    public Show createShow(ShowRequest request) {

        Event event = eventRepository.findById(request.getEventId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Event not found with id " + request.getEventId()));
        Venue venue = venueRepository.findById(request.getVenueId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Venue not found with id " + request.getVenueId()));

        Show show = new Show();
        show.setEvent(event);
        show.setVenue(venue);
        show.setStartTime(request.getStartTime());
        show.setEndTime(request.getEndTime());
        show.setPrice(request.getPrice());

        return showRepository.save(show);
    }

    @Override
    public Show getShow(Long id) {
        return showRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Show not found with id " + id));
    }
}
