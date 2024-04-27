package io.swagger.api;

import io.swagger.jpa.ReservationRepository;
import io.swagger.jpa.ShowtimeRepository;
import io.swagger.jpa.TheaterBoxRepository;
import io.swagger.model.Reservation;
import io.swagger.model.ReservationModifyBody;
import io.swagger.model.ReservationReserveBody;
import io.swagger.model.Showtime;
import io.swagger.model.TheaterBox;
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
import java.util.Optional;

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
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Valid;
import javax.validation.constraints.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
//import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import org.threeten.bp.OffsetDateTime;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-11T20:35:28.031354+01:00[Europe/London]")
@RestController
public class ReservationApiController implements ReservationApi {

	private static final Logger log = LoggerFactory.getLogger(ReservationApiController.class);

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private TheaterBoxRepository theaterBoxRepository;

	@org.springframework.beans.factory.annotation.Autowired
	public ReservationApiController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

		public ResponseEntity<Reservation> reservationCancelDelete(@NotNull @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "theater_box", required = true) Long theaterBoxId
	,@NotNull @Parameter(in = ParameterIn.QUERY, description = "ID of the showtime." ,required=true,schema=@Schema()) @Valid @RequestParam(value = "showtime_id", required = true) Long showtimeId
	,@NotNull @Parameter(in = ParameterIn.QUERY, description = "ID of the reservation to cancel." ,required=true,schema=@Schema()) @Valid @RequestParam(value = "reservation_id", required = true) Long reservationId
	) {
					// Check if the reservation exists
					Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
					if (!optionalReservation.isPresent()) {
						return ResponseEntity.notFound().build();
					}
					Reservation reservation = optionalReservation.get();

					// Check if the showtime has already occurred
					Optional<Showtime> optionalShowtime = showtimeRepository.findById(showtimeId);
					if (optionalShowtime.isPresent()) {
						Showtime showtime = optionalShowtime.get();
						if (showtime.getDateTime().isBefore(OffsetDateTime.now())) {
							return ResponseEntity.status(HttpStatus.CONFLICT).build();
						}
					} else {
						return ResponseEntity.notFound().build();
					}

					// Update the reserved seats in the theater box
					TheaterBox retrievedTheaterBox = reservation.getTheaterBox();
					retrievedTheaterBox.setReservedSeats(retrievedTheaterBox.getReservedSeats() - reservation.getSeatsReserved());
					theaterBoxRepository.save(retrievedTheaterBox);

					// Delete the reservation
					reservationRepository.delete(reservation);

					return ResponseEntity.ok(reservation);
				}
		

		public ResponseEntity<Reservation> reservationModifyPut(@NotNull @Parameter(in = ParameterIn.QUERY, description = "Number of the theater box." ,required=true,schema=@Schema()) @Valid @RequestParam(value = "theater_box", required = true) Long theaterBoxId
				,@NotNull @Parameter(in = ParameterIn.QUERY, description = "ID of the showtime." ,required=true,schema=@Schema()) @Valid @RequestParam(value = "showtime_id", required = true) Long showtimeId
				,@NotNull @Parameter(in = ParameterIn.QUERY, description = "ID of the reservation to modify." ,required=true,schema=@Schema()) @Valid @RequestParam(value = "reservation_id", required = true) Long reservationId
				,@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody ReservationModifyBody body
				) {
					// Check if the reservation exists
					Optional<Reservation> optionalReservation = reservationRepository.findById(reservationId);
					if (!optionalReservation.isPresent()) {
						return ResponseEntity.notFound().build();
					}
					Reservation reservation = optionalReservation.get();

					// Check if the showtime has already occurred
					Optional<Showtime> optionalShowtime = showtimeRepository.findById(showtimeId);
					if (optionalShowtime.isPresent()) {
						Showtime showtime = optionalShowtime.get();
						if (showtime.getDateTime().isBefore(OffsetDateTime.now())) {
							return ResponseEntity.status(HttpStatus.CONFLICT).build();
						}
					} else {
						return ResponseEntity.notFound().build();
					}

					// Check if the requested number of seats is available
					int availableSeats = reservation.getTheaterBox().getTotalSeats() - reservation.getTheaterBox().getReservedSeats() + reservation.getSeatsReserved();
					if (body.getSeats() > availableSeats) {
						return ResponseEntity.status(HttpStatus.CONFLICT).build();
					}

					// Update the reservation
					int seatsChange = body.getSeats() - reservation.getSeatsReserved();
					reservation.setSeatsReserved(body.getSeats());
					reservationRepository.save(reservation);

					// Update the reserved seats in the theater box
					TheaterBox retrievedTheaterBox = reservation.getTheaterBox();
					retrievedTheaterBox.setReservedSeats(retrievedTheaterBox.getReservedSeats() + seatsChange);
					theaterBoxRepository.save(retrievedTheaterBox);

					return ResponseEntity.ok(reservation);
		}
			

	public ResponseEntity<Reservation> reservationReservePost(
			@NotNull @Parameter(in = ParameterIn.QUERY, description = "", required = true, schema = @Schema()) @Valid @RequestParam(value = "theater_box", required = true) Long theaterBoxId,
			@NotNull @Parameter(in = ParameterIn.QUERY, description = "ID of the showtime.", required = true, schema = @Schema()) @Valid @RequestParam(value = "showtime_id", required = true) Long showtimeId,
			@Parameter(in = ParameterIn.DEFAULT, description = "", required = true, schema = @Schema()) @Valid @RequestBody ReservationReserveBody body) {
				// Check if the showtime exists
				Optional<Showtime> optionalShowtime = showtimeRepository.findById(showtimeId);
				if (!optionalShowtime.isPresent()) {
					return ResponseEntity.notFound().build();
				}
				Showtime showtime = optionalShowtime.get();

				// Check if the theater box exists
				Optional<TheaterBox> optionalTheaterBox = theaterBoxRepository.findById(theaterBoxId);
				if (!optionalTheaterBox.isPresent()) {
					return ResponseEntity.notFound().build();
				}
				TheaterBox retrievedTheaterBox = optionalTheaterBox.get();

				// Check if the requested number of seats is available
				int availableSeats = retrievedTheaterBox.getTotalSeats() - retrievedTheaterBox.getReservedSeats();
				if (body.getSeats() > availableSeats) {
					return ResponseEntity.status(HttpStatus.CONFLICT).build();
				}

				// Create the reservation
				Reservation reservation = new Reservation();
				reservation.setShowtime(showtime);
				reservation.setTheaterBox(retrievedTheaterBox);
				reservation.setSeatsReserved(body.getSeats());
				reservationRepository.save(reservation);

				// Update the reserved seats in the theater box
				retrievedTheaterBox.setReservedSeats(retrievedTheaterBox.getReservedSeats() + body.getSeats());
				theaterBoxRepository.save(retrievedTheaterBox);

				return ResponseEntity.ok(reservation);
			}
	}



