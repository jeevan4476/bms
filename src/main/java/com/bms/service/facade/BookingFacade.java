package com.bms.service.facade;

import com.bms.dto.BookingRequest;
import com.bms.dto.BookingResponse;
import com.bms.entity.Payment;

public interface BookingFacade {
    BookingResponse createBooking(BookingRequest request);
    Payment processPayment(Long bookingId);
}
