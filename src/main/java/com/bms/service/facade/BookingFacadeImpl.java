package com.bms.service.facade;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bms.dto.BookingRequest;
import com.bms.dto.BookingResponse;
import com.bms.entity.Payment;
import com.bms.service.BookingService;

/**
 * Facade Pattern: Simplifies the complex interactions between different sub-systems
 * (Booking Engine, Locks, Payment Engine).
 */
@Service
public class BookingFacadeImpl implements BookingFacade {

    private static final Logger log = LoggerFactory.getLogger(BookingFacadeImpl.class);

    private final BookingService bookingService;

    public BookingFacadeImpl(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Override
    @Transactional
    public BookingResponse createBooking(BookingRequest request) {
        log.info("Facade mapping: Delegating booking creation to BookingService");
        return bookingService.createBooking(request);
    }

    @Override
    @Transactional
    public Payment processPayment(Long bookingId) {
        log.info("Facade mapping: Delegating payment processing (BookingService coordinates with PaymentService)");
        return bookingService.processPayment(bookingId);
    }
}
