package io.swagger.configuration;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import io.swagger.jpa.MembershipRepository;
import io.swagger.jpa.MovieRepository;
import io.swagger.jpa.ReservationRepository;
import io.swagger.jpa.ShowtimeRepository;
import io.swagger.jpa.TheaterBoxRepository;
import io.swagger.model.Membership;
import io.swagger.model.Movie;
import io.swagger.model.Reservation;
import io.swagger.model.Showtime;
import io.swagger.model.TheaterBox;

@Configuration
public class DatabaseInitialization {
    private static final Logger log = LoggerFactory.getLogger(DatabaseInitialization.class);

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner initDatabase(MovieRepository movieRepository, MembershipRepository membershipRepository,
                                          TheaterBoxRepository theaterBoxRepository, ShowtimeRepository showtimeRepository,
                                          ReservationRepository reservationRepository) {
        log.info("Preloading data");

        return args -> {
            // Movies
            Movie movie1 = new Movie("Dune: Part Two", "Denis Villeneuve", "Action", "PG-13", "2h46m", LocalDate.parse("2024-02-06"), new BigDecimal(8.7), true, false);
            Movie movie2 = new Movie("The Matrix Resurrections", "Lana Wachowski", "Sci-Fi", "R", "2h28m", LocalDate.parse("2024-01-15"), new BigDecimal(7.5), true, false);
            Movie movie3 = new Movie("The Batman", "Matt Reeves", "Action", "PG-13", "2h56m", LocalDate.parse("2023-11-20"), new BigDecimal(8.2), false, true);
            movieRepository.save(movie1);
            movieRepository.save(movie2);
            movieRepository.save(movie3);

            // Memberships
            Membership membership1 = new Membership(1L, "Jane", "Doe", "jane.doe@email.com", passwordEncoder.encode("test123"), "ROLE_ADMIN");
            Membership membership2 = new Membership(2L, "John", "Smith", "john.smith@email.com", passwordEncoder.encode("password"), "ROLE_MEMBER");
            Membership membership3 = new Membership(3L, "Alice", "Johnson", "alice.johnson@email.com", passwordEncoder.encode("123456"), "ROLE_MEMBER");
            membershipRepository.save(membership1);
            membershipRepository.save(membership2);
            membershipRepository.save(membership3);

            // Theater Boxes
            TheaterBox theaterBox1 = new TheaterBox(1L, 1, 50, 5, 12.99f);

            //create theater box with no available seats so reservations can't be made
            TheaterBox theaterBox2 = new TheaterBox(2L, 2, 60, 60, 14.99f);
            TheaterBox theaterBox3 = new TheaterBox(3L, 3, 70, 2, 16.99f);
            theaterBoxRepository.save(theaterBox1);
            theaterBoxRepository.save(theaterBox2);
            theaterBoxRepository.save(theaterBox3);

            // Showtimes
            Showtime showtime1 = new Showtime(1L, LocalDateTime.parse("2024-02-06T10:30:00"), movie1, theaterBox1);
            Showtime showtime2 = new Showtime(2L, LocalDateTime.parse("2024-02-06T14:00:00"), movie1, theaterBox2);
            Showtime showtime3 = new Showtime(3L, LocalDateTime.parse("2024-02-06T18:30:00"), movie2, theaterBox3);
            showtimeRepository.save(showtime1);
            showtimeRepository.save(showtime2);
            showtimeRepository.save(showtime3);

            // Reservations
            Reservation reservation1 = new Reservation(1L, showtime1, 3, membership2);
            Reservation reservation2 = new Reservation(2L, showtime1, 2, membership2);
            Reservation reservation3 = new Reservation(3L, showtime2, 60,membership2);
            Reservation reservation4 = new Reservation(4L, showtime3, 2, membership2);
            reservationRepository.save(reservation1);
            reservationRepository.save(reservation2);
            reservationRepository.save(reservation3);
            reservationRepository.save(reservation4);
        };
    }
}