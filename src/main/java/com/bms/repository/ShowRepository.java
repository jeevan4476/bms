package com.bms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bms.entity.Show;

public interface ShowRepository extends JpaRepository<Show, Long> {
    List<Show> findByEventId(Long eventId);
}