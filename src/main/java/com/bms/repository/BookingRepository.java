package com.bms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bms.entity.Booking;

public interface BookingRepository extends JpaRepository<Booking, Long> {
}