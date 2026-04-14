package com.bms.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.bms.entity.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByOrganizerId(Long id);
}