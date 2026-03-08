package com.bms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bms.entity.BookingSeat;

public interface BookingSeatRepository extends JpaRepository<BookingSeat, Long> {
}