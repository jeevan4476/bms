package com.bms.service;

import java.time.LocalDateTime;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.bms.entity.Booking;
import com.bms.entity.Payment;
import com.bms.entity_enums.PaymentStatus;
import com.bms.repository.PaymentRepository;
import com.bms.service.event.PaymentEvent;

@Service
public class PaymentService {

    private static final Logger log = LoggerFactory.getLogger(PaymentService.class);

    private final PaymentRepository paymentRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final ReceiptService receiptService;

    public PaymentService(PaymentRepository paymentRepository, 
                          ApplicationEventPublisher eventPublisher,
                          ReceiptService receiptService) {
        this.paymentRepository = paymentRepository;
        this.eventPublisher = eventPublisher;
        this.receiptService = receiptService;
    }

    public java.util.List<com.bms.dto.TransactionHistoryDTO> getTransactionHistory(Long userId) {
        log.info("Fetching transaction history for userId: {}", userId);
        return paymentRepository.findByBookingUserId(userId).stream()
                .map(p -> new com.bms.dto.TransactionHistoryDTO(
                        p.getBooking().getId(),
                        p.getBooking().getShow().getEvent().getTitle(),
                        p.getAmount(),
                        p.getStatus().name(),
                        p.getPaymentTime()
                )).collect(java.util.stream.Collectors.toList());
    }

    public Payment processPayment(Booking booking) {
        log.info("Processing payment: bookingId={}, amount={}", booking.getId(), booking.getTotalAmount());

        Payment payment = new Payment();
        payment.setBooking(booking);
        payment.setAmount(booking.getTotalAmount());
        payment.setStatus(PaymentStatus.PENDING);
        payment.setPaymentTime(LocalDateTime.now());

        payment = paymentRepository.save(payment);

        simulateGatewayDelay();

        boolean paymentResult = simulatePayment();

        if (paymentResult) {
            payment.setStatus(PaymentStatus.SUCCESS);
            log.info("Payment succeeded: paymentId={}, bookingId={}", payment.getId(), booking.getId());
        } else {
            payment.setStatus(PaymentStatus.FAILED);
            log.warn("Payment failed: paymentId={}, bookingId={}", payment.getId(), booking.getId());
        }

        payment = paymentRepository.save(payment);
        
        // Publish Event for Observer Pattern
        eventPublisher.publishEvent(new PaymentEvent(this, payment));
        
        return payment;
    }

    private boolean simulatePayment() {
        Random random = new Random();
        return random.nextInt(10) < 8; // 80% success rate
    }

    private void simulateGatewayDelay() {
        try {
            Thread.sleep(2000); // 2 second delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
