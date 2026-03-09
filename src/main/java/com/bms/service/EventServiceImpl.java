package com.bms.service;

import org.springframework.stereotype.Service;

import com.bms.dto.EventRequest;
import com.bms.entity.Event;
import com.bms.exception.ResourceNotFoundException;
import com.bms.repository.EventRepository;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public Event createEvent(EventRequest request) {

        Event event = new Event();
        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setEventType(request.getEventType());
        event.setDurationMinutes(request.getDurationMinutes());
        event.setImageUrl(request.getImageUrl());

        return eventRepository.save(event);
    }

    @Override
    public Event updateEvent(Long id, EventRequest request) {

        Event event = eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + id));

        event.setTitle(request.getTitle());
        event.setDescription(request.getDescription());
        event.setEventType(request.getEventType());
        event.setDurationMinutes(request.getDurationMinutes());
        event.setImageUrl(request.getImageUrl());

        return eventRepository.save(event);
    }

    @Override
    public void deleteEvent(Long id) {
        eventRepository.deleteById(id);
    }

    @Override
    public Event getEvent(Long id) {
        return eventRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Event not found with id " + id));
    }
}
