package com.bms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bms.entity.User;

public interface SeatRepository extends JpaRepository<User, Long> {
}