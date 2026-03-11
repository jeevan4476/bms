package com.bms.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.bms.dto.BookingRequest;
import com.bms.dto.BookingResponse;
import com.bms.entity.Booking;
import com.bms.entity.BookingSeat;
import com.bms.entity.Seat;
import com.bms.entity.Show;
import com.bms.entity.User;
import com.bms.entity_enums.BookingStatus;
import com.bms.repository.BookingRepository;
import com.bms.repository.BookingSeatRepository;
import com.bms.repository.SeatRepository;
import com.bms.repository.ShowRepository;
import com.bms.repository.UserRepository;

@Service
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final BookingSeatRepository bookingSeatRepository;
    private final UserRepository userRepository;
    private final ShowRepository showRepository;
    private final SeatRepository seatRepository;

    public BookingServiceImpl(
            BookingRepository bookingRepository,
            BookingSeatRepository bookingSeatRepository,
            UserRepository userRepository,
            ShowRepository showRepository,
            SeatRepository seatRepository) {

        this.bookingRepository = bookingRepository;
        this.bookingSeatRepository = bookingSeatRepository;
        this.userRepository = userRepository;
        this.showRepository = showRepository;
        this.seatRepository = seatRepository;
    }

    @Override
    public BookingResponse createBooking(BookingRequest request) {

        User user = userRepository.findById(request.getUserId()).orElseThrow();

        Show show = showRepository.findById(request.getShowId()).orElseThrow();

        List<Seat> seats = seatRepository.findAllById(request.getSeatIds());

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setShow(show);
        booking.setBookingTime(LocalDateTime.now());
        booking.setStatus(BookingStatus.CONFIRMED);

        booking = bookingRepository.save(booking);

        double totalAmount = 0;

        List<Long> seatIds = new ArrayList<>();

        for (Seat seat : seats) {

            BookingSeat bookingSeat = new BookingSeat();
            bookingSeat.setBooking(booking);
            bookingSeat.setSeat(seat);
            bookingSeat.setPrice(show.getPrice());

            bookingSeatRepository.save(bookingSeat);

            totalAmount += show.getPrice();
            seatIds.add(seat.getId());
        }

        booking.setTotalAmount(totalAmount);

        bookingRepository.save(booking);

        return new BookingResponse(
                booking.getId(),
                booking.getStatus(),
                totalAmount,
                seatIds
        );
    }

    @Override
    public BookingResponse getBooking(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId).orElseThrow();

        List<BookingSeat> bookingSeats =
                bookingSeatRepository.findByBookingId(bookingId);

        List<Long> seatIds = new ArrayList<>();

        for (BookingSeat bs : bookingSeats) {
            seatIds.add(bs.getSeat().getId());
        }

        return new BookingResponse(
                booking.getId(),
                booking.getStatus(),
                booking.getTotalAmount(),
                seatIds
        );
    }

    @Override
    public List<BookingResponse> getUserBookings(Long userId) {

        List<Booking> bookings = bookingRepository.findByUserId(userId);

        List<BookingResponse> responses = new ArrayList<>();

        for (Booking booking : bookings) {

            List<BookingSeat> bookingSeats =
                    bookingSeatRepository.findByBookingId(booking.getId());

            List<Long> seatIds = new ArrayList<>();

            for (BookingSeat bs : bookingSeats) {
                seatIds.add(bs.getSeat().getId());
            }

            responses.add(
                    new BookingResponse(
                            booking.getId(),
                            booking.getStatus(),
                            booking.getTotalAmount(),
                            seatIds
                    )
            );
        }

        return responses;
    }

    @Override
    public void cancelBooking(Long bookingId) {

        Booking booking = bookingRepository.findById(bookingId).orElseThrow();

        booking.setStatus(BookingStatus.CANCELLED);

        bookingRepository.save(booking);
    }
}