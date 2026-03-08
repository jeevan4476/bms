package com.bms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bms.entity.Seat;

public interface SeatRepository extends JpaRepository<Seat, Long> {
}