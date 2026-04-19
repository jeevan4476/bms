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

        EventRequest movie2Req = new EventRequest();
        movie2Req.setTitle("Dune: Part Two");
        movie2Req.setDescription("Paul Atreides unites with Chani and the Fremen while on a warpath of revenge against the conspirators who destroyed his family.");
        movie2Req.setEventType(EventType.MOVIE);
        movie2Req.setDurationMinutes(166);
        movie2Req.setImageUrl("https://picsum.photos/seed/dune/400/250");
        Event movie2 = eventService.createEvent(movie2Req);

        EventRequest movie3Req = new EventRequest();
        movie3Req.setTitle("Oppenheimer");
        movie3Req.setDescription("The story of American scientist J. Robert Oppenheimer and his role in the development of the atomic bomb.");
        movie3Req.setEventType(EventType.MOVIE);
        movie3Req.setDurationMinutes(180);
        movie3Req.setImageUrl("https://picsum.photos/seed/oppenheimer/400/250");
        Event movie3 = eventService.createEvent(movie3Req);

        EventRequest concertReq = new EventRequest();
        concertReq.setTitle("Coldplay: Music of the Spheres");
        concertReq.setDescription("Experience Coldplay live on their Music of the Spheres World Tour with stunning visuals and all-time hits.");
        concertReq.setEventType(EventType.CONCERT);
        concertReq.setDurationMinutes(180);
        concertReq.setImageUrl("https://picsum.photos/seed/coldplay/400/250");
        concertReq.setOrganizerId(clark.getId());
        Event concert = eventService.createEvent(concertReq);

        EventRequest concertReq2 = new EventRequest();
        concertReq2.setTitle("Ed Sheeran: Mathematics Tour");
        concertReq2.setDescription("Join Ed Sheeran for his record-breaking global tour performing classics and new hits alike.");
        concertReq2.setEventType(EventType.CONCERT);
        concertReq2.setDurationMinutes(150);
        concertReq2.setImageUrl("https://picsum.photos/seed/edsheeran/400/250");
        concertReq2.setOrganizerId(clark.getId());
        Event concert2 = eventService.createEvent(concertReq2);

        EventRequest concertReq3 = new EventRequest();
        concertReq3.setTitle("Dua Lipa: Future Nostalgia");
        concertReq3.setDescription("Get ready to dance the night away with Dua Lipa's global sensation tour.");
        concertReq3.setEventType(EventType.CONCERT);
        concertReq3.setDurationMinutes(130);
        concertReq3.setImageUrl("https://picsum.photos/seed/dualipa/400/250");
        concertReq3.setOrganizerId(clark.getId());
        Event concert3 = eventService.createEvent(concertReq3);

        EventRequest sportReq = new EventRequest();
        sportReq.setTitle("Premier League: Arsenal vs Chelsea");
        sportReq.setDescription("A thrilling London derby at the Emirates Stadium. Two of the biggest clubs in English football face off.");
        sportReq.setEventType(EventType.SPORT);
        sportReq.setDurationMinutes(120);
        sportReq.setImageUrl("https://picsum.photos/seed/football/400/250");
        sportReq.setOrganizerId(clark.getId());
        Event sport = eventService.createEvent(sportReq);

        EventRequest sport2Req = new EventRequest();
        sport2Req.setTitle("NBA Finals: Lakers vs Celtics");
        sport2Req.setDescription("The ultimate basketball rivalry resumes in Game 7 of the NBA Finals.");
        sport2Req.setEventType(EventType.SPORT);
        sport2Req.setDurationMinutes(150);
        sport2Req.setImageUrl("https://picsum.photos/seed/nba/400/250");
        sport2Req.setOrganizerId(clark.getId());
        Event sport2 = eventService.createEvent(sport2Req);

        EventRequest sport3Req = new EventRequest();
        sport3Req.setTitle("Wimbledon Men's Final");
        sport3Req.setDescription("Witness history unfold on the grass courts of Wimbledon for the Grand Slam decider.");
        sport3Req.setEventType(EventType.SPORT);
        sport3Req.setDurationMinutes(240);
        sport3Req.setImageUrl("https://picsum.photos/seed/tennis/400/250");
        sport3Req.setOrganizerId(clark.getId());
        Event sport3 = eventService.createEvent(sport3Req);

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
        movieShow1Req.setPrice(1250.00);
        Show movieShow1 = showService.createShow(movieShow1Req);

        ShowRequest movieShow2Req = new ShowRequest();
        movieShow2Req.setEventId(movie.getId());
        movieShow2Req.setVenueId(cinema.getId());
        movieShow2Req.setStartTime(tomorrow.plusHours(4));
        movieShow2Req.setEndTime(tomorrow.plusHours(4).plusMinutes(148));
        movieShow2Req.setPrice(1500.00);
        showService.createShow(movieShow2Req);

        ShowRequest concertShowReq = new ShowRequest();
        concertShowReq.setEventId(concert.getId());
        concertShowReq.setVenueId(concertArena.getId());
        concertShowReq.setStartTime(tomorrow.plusDays(2).withHour(19));
        concertShowReq.setEndTime(tomorrow.plusDays(2).withHour(22));
        concertShowReq.setPrice(8500.00);
        Show concertShow = showService.createShow(concertShowReq);

        ShowRequest concertShowReq2 = new ShowRequest();
        concertShowReq2.setEventId(concert2.getId());
        concertShowReq2.setVenueId(concertArena.getId());
        concertShowReq2.setStartTime(tomorrow.plusDays(6).withHour(19));
        concertShowReq2.setEndTime(tomorrow.plusDays(6).withHour(21).withMinute(30));
        concertShowReq2.setPrice(7500.00);
        Show concertShow2 = showService.createShow(concertShowReq2);

        ShowRequest concertShowReq3 = new ShowRequest();
        concertShowReq3.setEventId(concert3.getId());
        concertShowReq3.setVenueId(concertArena.getId());
        concertShowReq3.setStartTime(tomorrow.plusDays(9).withHour(20));
        concertShowReq3.setEndTime(tomorrow.plusDays(9).withHour(22).withMinute(10));
        concertShowReq3.setPrice(6500.00);
        showService.createShow(concertShowReq3);

        ShowRequest sportShowReq = new ShowRequest();
        sportShowReq.setEventId(sport.getId());
        sportShowReq.setVenueId(stadium.getId());
        sportShowReq.setStartTime(tomorrow.plusDays(5).withHour(15));
        sportShowReq.setEndTime(tomorrow.plusDays(5).withHour(17));
        sportShowReq.setPrice(4500.00);
        Show sportShow = showService.createShow(sportShowReq);

        ShowRequest sport2ShowReq = new ShowRequest();
        sport2ShowReq.setEventId(sport2.getId());
        sport2ShowReq.setVenueId(stadium.getId());
        sport2ShowReq.setStartTime(tomorrow.plusDays(7).withHour(20));
        sport2ShowReq.setEndTime(tomorrow.plusDays(7).withHour(23));
        sport2ShowReq.setPrice(5500.00);
        showService.createShow(sport2ShowReq);

        ShowRequest sport3ShowReq = new ShowRequest();
        sport3ShowReq.setEventId(sport3.getId());
        sport3ShowReq.setVenueId(stadium.getId());
        sport3ShowReq.setStartTime(tomorrow.plusDays(10).withHour(13));
        sport3ShowReq.setEndTime(tomorrow.plusDays(10).withHour(17));
        sport3ShowReq.setPrice(8000.00);
        showService.createShow(sport3ShowReq);

        ShowRequest movie3ShowReq = new ShowRequest();
        movie3ShowReq.setEventId(movie2.getId());
        movie3ShowReq.setVenueId(cinema.getId());
        movie3ShowReq.setStartTime(tomorrow.plusDays(1).withHour(19));
        movie3ShowReq.setEndTime(tomorrow.plusDays(1).withHour(22));
        movie3ShowReq.setPrice(950.00);
        showService.createShow(movie3ShowReq);

        ShowRequest movie4ShowReq = new ShowRequest();
        movie4ShowReq.setEventId(movie3.getId());
        movie4ShowReq.setVenueId(cinema.getId());
        movie4ShowReq.setStartTime(tomorrow.plusDays(2).withHour(18));
        movie4ShowReq.setEndTime(tomorrow.plusDays(2).withHour(21));
        movie4ShowReq.setPrice(1100.00);
        showService.createShow(movie4ShowReq);

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
