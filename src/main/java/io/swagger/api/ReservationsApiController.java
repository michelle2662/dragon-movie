package io.swagger.api;

import io.swagger.jpa.MembershipRepository;
import io.swagger.jpa.ReservationRepository;
import io.swagger.jpa.ShowtimeRepository;
import io.swagger.jpa.TheaterBoxRepository;
import io.swagger.model.*;
import io.swagger.security.JwtTokenProvider;
import io.swagger.service.MembershipService;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.threeten.bp.LocalDateTime;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import javax.validation.constraints.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-27T23:58:07.589943-04:00[America/New_York]")
@RestController
public class ReservationsApiController implements ReservationsApi {

    private static final Logger log = LoggerFactory.getLogger(ReservationsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private TheaterBoxRepository theaterBoxRepository;

    @Autowired
    private MembershipRepository membershipRepository;

    @Autowired
    private MembershipService membershipService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @org.springframework.beans.factory.annotation.Autowired
    public ReservationsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<List<Reservation>> reservationsGet(@RequestHeader("Authorization") String token) {
        log.info("GET /reservations");

        String email = jwtTokenProvider.getUsernameFromToken(token.substring(7));
        Optional<Membership> optionalMembership = membershipRepository.findByEmail(email);

        if (!optionalMembership.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Membership membership = optionalMembership.get();

        List<Reservation> reservations = reservationRepository.findByMemberId(membership.getId());
        return ResponseEntity.ok(reservations);
        
    }

    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<Reservation> reservationsPost(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody ReservationsBody body, @RequestHeader("Authorization") String token
) {
        log.info("POST /reservations");

        String email = jwtTokenProvider.getUsernameFromToken(token.substring(7));
        Optional<Membership> optionalMembership = membershipRepository.findByEmail(email);

        System.out.println("email: " + email);

        if (!optionalMembership.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!showtimeRepository.existsById(body.getShowtimeId())) {
            return ResponseEntity.notFound().build();
        }
        Membership membership = optionalMembership.get();

        Reservation reservation = new Reservation();
        reservation.setShowtime(showtimeRepository.findById(body.getShowtimeId()).get());
        reservation.setSeatsReserved(body.getSeatsReserved());
        reservation.setMember(membership);


        TheaterBox theaterBox = theaterBoxRepository.findByBoxNumber(showtimeRepository.findById(body.getShowtimeId()).get().getTheaterBox().getBoxNumber());

        if (theaterBox.getReservedSeats() + body.getSeatsReserved() <= theaterBox.getTotalSeats()){
            theaterBox.setReservedSeats(body.getSeatsReserved() + theaterBox.getReservedSeats());
            theaterBoxRepository.save(theaterBox);
        } else {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        Reservation savedReservation = reservationRepository.save(reservation);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedReservation);
    }

    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<Void> reservationsReservationIdDelete(@Parameter(in = ParameterIn.PATH, description = "ID of the reservation to cancel.", required=true, schema=@Schema()) @PathVariable("reservation_id") Long reservationId, @RequestHeader("Authorization") String token
) {
        log.info("DELETE /reservations/{}", reservationId);

        String email = jwtTokenProvider.getUsernameFromToken(token.substring(7));
        Optional<Membership> optionalMembership = membershipRepository.findByEmail(email);

        if (!optionalMembership.isPresent()) {
            log.warn("Unauthorized access attempt. User not found with email: {}", email);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Membership membership = optionalMembership.get();

        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
        if (!optionalReservation.isPresent()) {
            log.warn("Reservation not found with ID: {}", reservationId);
            return ResponseEntity.notFound().build();
        }

        Reservation reservation = optionalReservation.get();
        if (!reservation.getMember().equals(membership)) {
            log.warn("Forbidden access attempt. User {} tried to delete reservation {}", email, reservationId);
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        reservationRepository.delete(reservation);
        log.info("Reservation deleted successfully. ID: {}", reservationId);

        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('MEMBER')")
    public ResponseEntity<Reservation> reservationsReservationIdPut(@Parameter(in = ParameterIn.PATH, description = "ID of the reservation to update.", required=true, schema=@Schema()) @PathVariable("reservation_id") Long reservationId
,@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody ReservationsReservationIdBody body, @RequestHeader("Authorization") String token
) {
        log.info("PUT /reservations/{}", reservationId);

        String email = jwtTokenProvider.getUsernameFromToken(token.substring(7));
        Optional<Membership> optionalMembership = membershipRepository.findByEmail(email);

        System.out.println(optionalMembership.get());

        if (!optionalMembership.isPresent()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Membership membership = optionalMembership.get();

        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
        if (!optionalReservation.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Reservation reservation = optionalReservation.get();
        if (!reservation.getMember().equals(membership)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (!showtimeRepository.existsById(body.getShowtimeId())) {
            return ResponseEntity.notFound().build();
        }

        long showtimeId = reservationRepository.findById(reservationId).get().getShowtime().getId();
        TheaterBox theaterBox = theaterBoxRepository.findByBoxNumber(showtimeRepository.findById(showtimeId).get().getTheaterBox().getBoxNumber());

        int diff = body.getSeatsReserved() - reservation.getSeatsReserved() ;

        if (theaterBox.getReservedSeats() + diff <= theaterBox.getTotalSeats()){
            reservation.setShowtime(showtimeRepository.findById(body.getShowtimeId()).get());
            reservation.setSeatsReserved(body.getSeatsReserved());
            theaterBox.setReservedSeats(theaterBox.getReservedSeats() + diff);
            theaterBoxRepository.save(theaterBox);

            Reservation updatedReservation = reservationRepository.save(reservation);
            return ResponseEntity.ok(updatedReservation);

        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

    }

}
