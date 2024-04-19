package io.swagger.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.swagger.model.Showtime;

@Repository
public interface ShowtimeRepository extends JpaRepository<Showtime, Long> {

}