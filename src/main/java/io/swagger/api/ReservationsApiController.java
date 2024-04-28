package io.swagger.api;

import io.swagger.jpa.MembershipRepository;
import io.swagger.jpa.ReservationRepository;
import io.swagger.jpa.ShowtimeRepository;
import io.swagger.model.Reservation;
import io.swagger.model.ReservationsBody;
import io.swagger.model.ReservationsReservationIdBody;
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
        Long memberId = membershipRepository.findIdByEmail(email).orElse(null);

        if (memberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<List<Reservation>> reservations = reservationRepository.findByMemberId(memberId);

        return ResponseEntity.ok(reservations);
    }

    public ResponseEntity<Reservation> reservationsPost(@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody ReservationsBody body, @RequestHeader("Authorization") String token
) {
        log.info("POST /reservations");

        String email = jwtTokenProvider.getUsernameFromToken(token.substring(7));
        Long memberId = membershipService.getMemberIdByEmail(email);

        if (memberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        if (!showtimeRepository.existsById(body.getShowtimeId())) {
            return ResponseEntity.notFound().build();
        }

        Reservation reservation = new Reservation();
        reservation.setShowtime(showtimeRepository.findById(body.getShowtimeId()).get());
        reservation.setSeatsReserved(body.getSeatsReserved());
        reservation.setMemberId(memberId);

        Reservation savedReservation = reservationRepository.save(reservation);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedReservation);
    }

    public ResponseEntity<Void> reservationsReservationIdDelete(@Parameter(in = ParameterIn.PATH, description = "ID of the reservation to cancel.", required=true, schema=@Schema()) @PathVariable("reservation_id") Long reservationId, @RequestHeader("Authorization") String token
) {
        log.info("DELETE /reservations/{}", reservationId);

        String email = jwtTokenProvider.getUsernameFromToken(token.substring(7));
        Long memberId = membershipService.getMemberIdByEmail(email);

        if (memberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
        if (!optionalReservation.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Reservation reservation = optionalReservation.get();
        if (!reservation.getMemberId().equals(memberId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        reservationRepository.delete(reservation);

        return ResponseEntity.noContent().build();
    }

    public ResponseEntity<Reservation> reservationsReservationIdPut(@Parameter(in = ParameterIn.PATH, description = "ID of the reservation to update.", required=true, schema=@Schema()) @PathVariable("reservation_id") Long reservationId
,@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody ReservationsReservationIdBody body, @RequestHeader("Authorization") String token
) {
        log.info("PUT /reservations/{}", reservationId);

        String email = jwtTokenProvider.getUsernameFromToken(token.substring(7));
        Long memberId = membershipService.getMemberIdByEmail(email);

        if (memberId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
        if (!optionalReservation.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        Reservation reservation = optionalReservation.get();
        if (!reservation.getMemberId().equals(memberId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        if (!showtimeRepository.existsById(body.getShowtimeId())) {
            return ResponseEntity.notFound().build();
        }

        reservation.setShowtime(showtimeRepository.findById(body.getShowtimeId()).get());
        reservation.setSeatsReserved(body.getSeatsReserved());

        Reservation updatedReservation = reservationRepository.save(reservation);

        return ResponseEntity.ok(updatedReservation);
    }

}
