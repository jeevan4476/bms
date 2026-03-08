package com.bms.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bms.entity.Show;

public interface ShowRepository extends JpaRepository<Show, Long> {
}