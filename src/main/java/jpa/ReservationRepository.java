package jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import io.swagger.model.Reservation;

//@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

}
