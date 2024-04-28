package io.swagger.jpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import io.swagger.model.Movie;
import io.swagger.model.Showtime;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

    @Query("SELECT s.movie FROM Showtime s WHERE s.id = :showtimeId")
    List<Movie> findMoviesByShowtimeId(@Param("showtimeId") Long showtimeId);

    List<Showtime> findByMovieId(Long movieId);

    boolean existsById(Integer showtimeId);

}