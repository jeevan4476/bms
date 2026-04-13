package com.bms.service.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.bms.entity.Booking;
import com.bms.entity.Payment;
import com.bms.entity_enums.BookingStatus;
import com.bms.entity_enums.PaymentStatus;
import com.bms.repository.BookingRepository;
import com.bms.repository.BookingSeatRepository;

@Component
public class PaymentEventListener {

    private static final Logger log = LoggerFactory.getLogger(PaymentEventListener.class);

    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;

    public PaymentEventListener(BookingRepository bookingRepository, BookingSeatRepository bookingSeatRepository) {
        this.bookingRepository = bookingRepository;
        this.bookingSeatRepository = bookingSeatRepository;
    }

    @EventListener
    @Transactional
    public void handlePaymentEvent(PaymentEvent event) {
        Payment payment = event.getPayment();
        Booking booking = payment.getBooking();

        log.info("Received PaymentEvent for bookingId={}, status={}", booking.getId(), payment.getStatus());

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            booking.setStatus(BookingStatus.CONFIRMED);
            log.info("Observer mapping: Payment SUCCESS for booking {}, assigned CONFIRMED status.", booking.getId());
        } else if (payment.getStatus() == PaymentStatus.FAILED) {
            booking.setStatus(BookingStatus.CANCELLED);
            bookingSeatRepository.deleteByBookingId(booking.getId());
            log.warn("Observer mapping: Payment FAILED for booking {}. Seats released.", booking.getId());
        }

        bookingRepository.save(booking);
    }
}
