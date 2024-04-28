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
       "JOIN s.theaterBox tb " +
       "WHERE s.dateTime BETWEEN :startTime AND :endTime " +
       "GROUP BY m.title")
    List<Object[]> findTotalRevenueByMovie(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);

    @Query("SELECT m FROM Movie m WHERE " +
            "(:rating IS NULL OR m.rating = :rating) AND " +
            "(:genre IS NULL OR m.genre = :genre) AND " +
            "(:title IS NULL OR m.title = :title) AND " +
            "(:length IS NULL OR m.length = :length) AND " +
            "(:releaseDate IS NULL OR m.releaseDate = :releaseDate) AND " +
            "(:director IS NULL OR m.director = :director) AND " +
            "(:reviewScore IS NULL OR m.reviewScore = :reviewScore)")
    List<Movie> findByOptionalAttributes(@Param("rating") String rating,
            @Param("genre") String genre,
            @Param("title") String title,
            @Param("length") String length,
            @Param("releaseDate") LocalDate releaseDate,
            @Param("director") String director,
            @Param("reviewScore") BigDecimal reviewScore);
}
