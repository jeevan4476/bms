package com.bms.config;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.bms.dto.BookingRequest;
import com.bms.dto.BookingResponse;
import com.bms.dto.EventRequest;
import com.bms.dto.ShowRequest;
import com.bms.dto.VenueRequest;
import com.bms.dto.VenueRequest.SectionRequest;
import com.bms.entity.Booking;
import com.bms.entity.Event;
import com.bms.entity.Show;
import com.bms.entity.User;
import com.bms.entity.Venue;
import com.bms.entity_enums.BookingStatus;
import com.bms.entity_enums.EventType;
import com.bms.entity_enums.SeatType;
import com.bms.entity_enums.UserRole;
import com.bms.entity_enums.VenueLayoutType;
import com.bms.repository.BookingRepository;
import com.bms.repository.SeatRepository;
import com.bms.service.AuthService;
import com.bms.service.BookingService;
import com.bms.service.EventService;
import com.bms.service.ShowService;
import com.bms.service.VenueService;

@Component
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final AuthService authService;
    private final EventService eventService;
    private final VenueService venueService;
    private final ShowService showService;
    private final BookingService bookingService;
    private final BookingRepository bookingRepository;
    private final SeatRepository seatRepository;

    public DataSeeder(AuthService authService,
                      EventService eventService,
                      VenueService venueService,
                      ShowService showService,
                      BookingService bookingService,
                      BookingRepository bookingRepository,
                      SeatRepository seatRepository) {
        this.authService = authService;
        this.eventService = eventService;
        this.venueService = venueService;
        this.showService = showService;
        this.bookingService = bookingService;
        this.bookingRepository = bookingRepository;
        this.seatRepository = seatRepository;
    }

    @Override
    public void run(String... args) {
        log.info("=== Seeding demo data ===");

        // --- Users ---
        User admin = new User();
        admin.setName("Admin User");
        admin.setEmail("admin@bms.com");
        admin.setPassword("admin123");
        admin.setRole(UserRole.ADMIN);
        admin = authService.register(admin);

        User alice = new User();
        alice.setName("Alice Johnson");
        alice.setEmail("alice@example.com");
        alice.setPassword("password");
        alice.setRole(UserRole.USER);
        alice = authService.register(alice);

        User bob = new User();
        bob.setName("Bob Smith");
        bob.setEmail("bob@example.com");
        bob.setPassword("password");
        bob.setRole(UserRole.USER);
        bob = authService.register(bob);

        User clark = new User();
        clark.setName("Clark Kent");
        clark.setEmail("clark@bms.com");
        clark.setPassword("clark123");
        clark.setRole(UserRole.ORGANIZER);
        clark = authService.register(clark);

        // --- Events ---
        EventRequest movieReq = new EventRequest();
        movieReq.setTitle("Inception");
        movieReq.setDescription("A mind-bending thriller by Christopher Nolan. Dom Cobb is a thief who steals corporate secrets through dream-sharing technology.");
        movieReq.setEventType(EventType.MOVIE);
        movieReq.setDurationMinutes(148);
        movieReq.setImageUrl("https://picsum.photos/seed/inception/400/250");
        Event movie = eventService.createEvent(movieReq);

        EventRequest concertReq = new EventRequest();
        concertReq.setTitle("Coldplay: Music of the Spheres");
        concertReq.setDescription("Experience Coldplay live on their Music of the Spheres World Tour with stunning visuals and all-time hits.");
        concertReq.setEventType(EventType.CONCERT);
        concertReq.setDurationMinutes(180);
        concertReq.setImageUrl("https://picsum.photos/seed/coldplay/400/250");
        concertReq.setOrganizerId(clark.getId());
        Event concert = eventService.createEvent(concertReq);

        EventRequest sportReq = new EventRequest();
        sportReq.setTitle("Premier League: Arsenal vs Chelsea");
        sportReq.setDescription("A thrilling London derby at the Emirates Stadium. Two of the biggest clubs in English football face off.");
        sportReq.setEventType(EventType.SPORT);
        sportReq.setDurationMinutes(120);
        sportReq.setImageUrl("https://picsum.photos/seed/football/400/250");
        sportReq.setOrganizerId(clark.getId());
        Event sport = eventService.createEvent(sportReq);

        // --- Venues ---

        // Theatre (IMAX Cinema Hall) - curved rows for movies
        VenueRequest cinemaReq = new VenueRequest();
        cinemaReq.setName("IMAX Cinema Hall");
        cinemaReq.setLocation("Downtown Mall, 5th Avenue");
        cinemaReq.setLayoutType(VenueLayoutType.THEATRE);
        cinemaReq.setSections(List.of(
            section("Balcony",   2, 8, SeatType.PREMIUM),
            section("Middle",    3, 10, SeatType.REGULAR),
            section("Front Row", 2, 8, SeatType.VIP)
        ));
        Venue cinema = venueService.createVenue(cinemaReq);

        // Concert Arena - radial/semi-circle layout
        VenueRequest concertArenaReq = new VenueRequest();
        concertArenaReq.setName("City Music Arena");
        concertArenaReq.setLocation("Olympic Park, North Road");
        concertArenaReq.setLayoutType(VenueLayoutType.CONCERT_ARENA);
        concertArenaReq.setSections(List.of(
            section("FLOOR",      3, 12, SeatType.VIP),
            section("CIRCLE",     4, 14, SeatType.PREMIUM),
            section("UPPER TIER", 3, 16, SeatType.REGULAR)
        ));
        Venue concertArena = venueService.createVenue(concertArenaReq);

        // Stadium - oval with named stands
        VenueRequest stadiumReq = new VenueRequest();
        stadiumReq.setName("Emirates Stadium");
        stadiumReq.setLocation("Highbury House, London");
        stadiumReq.setLayoutType(VenueLayoutType.STADIUM);
        stadiumReq.setSections(List.of(
            section("North Stand",     4, 12, SeatType.PREMIUM),
            section("South Stand",     4, 12, SeatType.REGULAR),
            section("East Stand",      3, 10, SeatType.VIP),
            section("West Stand",      3, 10, SeatType.PREMIUM),
            section("Away End",        2, 10, SeatType.REGULAR),
            section("Club Lounge",     2, 8,  SeatType.VIP)
        ));
        Venue stadium = venueService.createVenue(stadiumReq);

        // --- Shows ---
        LocalDateTime tomorrow = LocalDateTime.now().plusDays(1).withHour(14).withMinute(0).withSecond(0).withNano(0);

        ShowRequest movieShow1Req = new ShowRequest();
        movieShow1Req.setEventId(movie.getId());
        movieShow1Req.setVenueId(cinema.getId());
        movieShow1Req.setStartTime(tomorrow);
        movieShow1Req.setEndTime(tomorrow.plusMinutes(148));
        movieShow1Req.setPrice(12.50);
        Show movieShow1 = showService.createShow(movieShow1Req);

        ShowRequest movieShow2Req = new ShowRequest();
        movieShow2Req.setEventId(movie.getId());
        movieShow2Req.setVenueId(cinema.getId());
        movieShow2Req.setStartTime(tomorrow.plusHours(4));
        movieShow2Req.setEndTime(tomorrow.plusHours(4).plusMinutes(148));
        movieShow2Req.setPrice(15.00);
        showService.createShow(movieShow2Req);

        ShowRequest concertShowReq = new ShowRequest();
        concertShowReq.setEventId(concert.getId());
        concertShowReq.setVenueId(concertArena.getId());
        concertShowReq.setStartTime(tomorrow.plusDays(2).withHour(19));
        concertShowReq.setEndTime(tomorrow.plusDays(2).withHour(22));
        concertShowReq.setPrice(85.00);
        Show concertShow = showService.createShow(concertShowReq);

        ShowRequest sportShowReq = new ShowRequest();
        sportShowReq.setEventId(sport.getId());
        sportShowReq.setVenueId(stadium.getId());
        sportShowReq.setStartTime(tomorrow.plusDays(5).withHour(15));
        sportShowReq.setEndTime(tomorrow.plusDays(5).withHour(17));
        sportShowReq.setPrice(45.00);
        Show sportShow = showService.createShow(sportShowReq);

        // --- Bookings ---
        var cinemaSeats = seatRepository.findByVenueId(cinema.getId());
        var arenaSeats  = seatRepository.findByVenueId(concertArena.getId());
        var stadiumSeats = seatRepository.findByVenueId(stadium.getId());

        // Alice books 2 seats for the movie
        BookingRequest aliceMovieBooking = new BookingRequest();
        aliceMovieBooking.setUserId(alice.getId());
        aliceMovieBooking.setShowId(movieShow1.getId());
        aliceMovieBooking.setSeatIds(List.of(cinemaSeats.get(0).getId(), cinemaSeats.get(1).getId()));
        BookingResponse aliceBooking = bookingService.createBooking(aliceMovieBooking);
        Booking aliceBookingEntity = bookingRepository.findById(aliceBooking.getBookingId()).orElseThrow();
        aliceBookingEntity.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(aliceBookingEntity);
        log.info("Seeded CONFIRMED booking: id={}, user=alice, show=movie", aliceBooking.getBookingId());

        // Bob books 3 seats for the concert
        BookingRequest bobConcertBooking = new BookingRequest();
        bobConcertBooking.setUserId(bob.getId());
        bobConcertBooking.setShowId(concertShow.getId());
        bobConcertBooking.setSeatIds(List.of(arenaSeats.get(0).getId(), arenaSeats.get(1).getId(), arenaSeats.get(2).getId()));
        BookingResponse bobBooking = bookingService.createBooking(bobConcertBooking);
        Booking bobBookingEntity = bookingRepository.findById(bobBooking.getBookingId()).orElseThrow();
        bobBookingEntity.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(bobBookingEntity);
        log.info("Seeded CONFIRMED booking: id={}, user=bob, show=concert", bobBooking.getBookingId());

        // Alice books 1 seat for the sport event (left as PENDING)
        BookingRequest aliceSportBooking = new BookingRequest();
        aliceSportBooking.setUserId(alice.getId());
        aliceSportBooking.setShowId(sportShow.getId());
        aliceSportBooking.setSeatIds(List.of(stadiumSeats.get(10).getId()));
        BookingResponse aliceSportResp = bookingService.createBooking(aliceSportBooking);
        log.info("Seeded PENDING booking: id={}, user=alice, show=sport", aliceSportResp.getBookingId());

        log.info("=== Demo data seeding complete ===");
        log.info("Login credentials:");
        log.info("  Admin: admin@bms.com / admin123");
        log.info("  Org:   clark@bms.com / clark123");
        log.info("  User:  alice@example.com / password");
        log.info("  User:  bob@example.com / password");
    }

    private SectionRequest section(String name, int rows, int seatsPerRow, SeatType type) {
        SectionRequest s = new SectionRequest();
        s.setName(name);
        s.setRows(rows);
        s.setSeatsPerRow(seatsPerRow);
        s.setSeatType(type);
        return s;
    }
}
