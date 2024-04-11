package io.swagger.api;

import io.swagger.model.Reservation;
import io.swagger.model.ReservationModifyBody;
import io.swagger.model.ReservationReserveBody;
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

import javax.validation.Valid;
import javax.validation.constraints.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-11T20:35:28.031354+01:00[Europe/London]")
@RestController
public class ReservationApiController implements ReservationApi {

    private static final Logger log = LoggerFactory.getLogger(ReservationApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public ReservationApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Reservation> reservationCancelDelete(@NotNull @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "theater_box", required = true) TheaterBox theaterBox
,@NotNull @Parameter(in = ParameterIn.QUERY, description = "ID of the showtime." ,required=true,schema=@Schema()) @Valid @RequestParam(value = "showtime_id", required = true) String showtimeId
,@NotNull @Parameter(in = ParameterIn.QUERY, description = "ID of the reservation to cancel." ,required=true,schema=@Schema()) @Valid @RequestParam(value = "reservation_id", required = true) String reservationId
,@Parameter(in = ParameterIn.HEADER, description = "Member's access token for authorization." ,required=true,schema=@Schema()) @RequestHeader(value="access_token", required=true) String accessToken
) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Reservation>(objectMapper.readValue("{\n  \"theater_box\" : {\n    \"box_number\" : 0,\n    \"reserved_seats\" : 1,\n    \"ticket_price\" : 5.962134,\n    \"total_seats\" : 6\n  },\n  \"showtime_id\" : \"\",\n  \"id\" : \"id\"\n}", Reservation.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Reservation>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Reservation>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Reservation> reservationModifyPut(@Parameter(in = ParameterIn.HEADER, description = "Member's access token for authorization." ,required=true,schema=@Schema()) @RequestHeader(value="access_token", required=true) String accessToken
,@NotNull @Parameter(in = ParameterIn.QUERY, description = "Number of the theater box." ,required=true,schema=@Schema()) @Valid @RequestParam(value = "theater_box", required = true) TheaterBox theaterBox
,@NotNull @Parameter(in = ParameterIn.QUERY, description = "ID of the showtime." ,required=true,schema=@Schema()) @Valid @RequestParam(value = "showtime_id", required = true) String showtimeId
,@NotNull @Parameter(in = ParameterIn.QUERY, description = "ID of the reservation to modify." ,required=true,schema=@Schema()) @Valid @RequestParam(value = "reservation_id", required = true) String reservationId
,@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody ReservationModifyBody body
) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Reservation>(objectMapper.readValue("{\n  \"theater_box\" : {\n    \"box_number\" : 0,\n    \"reserved_seats\" : 1,\n    \"ticket_price\" : 5.962134,\n    \"total_seats\" : 6\n  },\n  \"showtime_id\" : \"\",\n  \"id\" : \"id\"\n}", Reservation.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Reservation>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Reservation>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Reservation> reservationReservePost(@Parameter(in = ParameterIn.HEADER, description = "Member's access token for authorization." ,required=true,schema=@Schema()) @RequestHeader(value="access_token", required=true) String accessToken
,@NotNull @Parameter(in = ParameterIn.QUERY, description = "" ,required=true,schema=@Schema()) @Valid @RequestParam(value = "theater_box", required = true) TheaterBox theaterBox
,@NotNull @Parameter(in = ParameterIn.QUERY, description = "ID of the showtime." ,required=true,schema=@Schema()) @Valid @RequestParam(value = "showtime_id", required = true) String showtimeId
,@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody ReservationReserveBody body
) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Reservation>(objectMapper.readValue("{\n  \"theater_box\" : {\n    \"box_number\" : 0,\n    \"reserved_seats\" : 1,\n    \"ticket_price\" : 5.962134,\n    \"total_seats\" : 6\n  },\n  \"showtime_id\" : \"\",\n  \"id\" : \"id\"\n}", Reservation.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Reservation>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Reservation>(HttpStatus.NOT_IMPLEMENTED);
    }

}
