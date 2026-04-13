package com.bms.service.factory;

import org.springframework.stereotype.Component;
import java.time.LocalDateTime;
import com.bms.entity.Event;

import com.bms.entity_enums.EventType;

/**
 * Factory Method Pattern: Centralizes creation of Event objects.
 * Encapsulates instantiation logic based on event domain rules.
 */
@Component
public class EventFactory {

    public Event createEvent(String name, String description, String category) {
        Event event = new Event();
        event.setTitle(name);
        event.setDescription(description);
        
        // This simulates a factory acting on an internal parameter or type
        if ("MOVIE".equalsIgnoreCase(category)) {
            event.setEventType(EventType.MOVIE);
        } else if ("CONCERT".equalsIgnoreCase(category)) {
            event.setEventType(EventType.CONCERT);
        } else {
            event.setEventType(EventType.SPORT);
        }
        
        return event;
    }
}
