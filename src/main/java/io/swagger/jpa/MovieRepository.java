package io.swagger.jpa;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.swagger.model.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("SELECT m.title, SUM(tb.ticketPrice * r.seatsReserved) AS totalRevenue " +
    "FROM Movie m " +
    "JOIN Showtime s ON m.id = s.movie.id " +
    "JOIN Reservation r ON s.id = r.showtime.id " +
    "JOIN TheaterBox tb ON r.theaterBox.id = tb.id " +
    "WHERE s.dateTime BETWEEN :startTime AND :endTime " +
    "GROUP BY m.title")
    List<Object[]> findTotalRevenueByMovie(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
}
