package com.bms.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.bms.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByBookingUserId(Long userId);
}