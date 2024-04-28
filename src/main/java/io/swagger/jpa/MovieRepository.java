package io.swagger.jpa;

import org.threeten.bp.LocalDate;
import org.threeten.bp.LocalDateTime;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.swagger.model.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {
    @Query("SELECT m.title, ROUND(SUM(tb.ticketPrice * r.seatsReserved), 2) AS totalRevenue " +
       "FROM Movie m " +
       "JOIN m.showtimes s " +
       "JOIN s.reservations r " +
       "JOIN r.theaterBox tb " +
       "WHERE s.dateTime BETWEEN :startTime AND :endTime " +
       "GROUP BY m.title")
    List<Object[]> findTotalRevenueByMovie(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    List<Movie> findByAttributes(String rating, String genre, String title, String length, LocalDate releaseDate, String director, BigDecimal reviewScore);
}
