package com.bms.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bms.entity.BookingSeat;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, Long> {

    List<BookingSeat> findByBookingId(Long bookingId);

}