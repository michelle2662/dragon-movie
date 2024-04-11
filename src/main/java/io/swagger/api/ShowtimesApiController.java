package io.swagger.api;

import io.swagger.model.Movie;
import io.swagger.model.Showtime;
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
public class ShowtimesApiController implements ShowtimesApi {

    private static final Logger log = LoggerFactory.getLogger(ShowtimesApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public ShowtimesApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<List<Showtime>> showtimesGet() {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Showtime>>(objectMapper.readValue("[ {\n  \"date_time\" : \"2000-01-23T04:56:07.000+00:00\",\n  \"movie\" : {\n    \"upcomingRelease\" : false,\n    \"currentlyPlaying\" : true,\n    \"reviewScore\" : 8.9,\n    \"releaseDate\" : \"2023-07-21T00:00:00.000+00:00\",\n    \"director\" : \"Christopher Nolan\",\n    \"genre\" : \"Thriller\",\n    \"rating\" : \"R\",\n    \"length\" : \"3h5m\",\n    \"title\" : \"Oppenheimer\"\n  },\n  \"theater_box\" : {\n    \"box_number\" : 0,\n    \"reserved_seats\" : 1,\n    \"ticket_price\" : 5.962134,\n    \"total_seats\" : 6\n  },\n  \"id\" : \"id\"\n}, {\n  \"date_time\" : \"2000-01-23T04:56:07.000+00:00\",\n  \"movie\" : {\n    \"upcomingRelease\" : false,\n    \"currentlyPlaying\" : true,\n    \"reviewScore\" : 8.9,\n    \"releaseDate\" : \"2023-07-21T00:00:00.000+00:00\",\n    \"director\" : \"Christopher Nolan\",\n    \"genre\" : \"Thriller\",\n    \"rating\" : \"R\",\n    \"length\" : \"3h5m\",\n    \"title\" : \"Oppenheimer\"\n  },\n  \"theater_box\" : {\n    \"box_number\" : 0,\n    \"reserved_seats\" : 1,\n    \"ticket_price\" : 5.962134,\n    \"total_seats\" : 6\n  },\n  \"id\" : \"id\"\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Showtime>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Showtime>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> showtimesPost(@Parameter(in = ParameterIn.HEADER, description = "Admin's access token for authorization." ,required=true,schema=@Schema()) @RequestHeader(value="access_token", required=true) String accessToken
,@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody Showtime body
) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> showtimesShowtimeIdDelete(@Parameter(in = ParameterIn.PATH, description = "ID of the showtime to delete.", required=true, schema=@Schema()) @PathVariable("showtime_id") String showtimeId
,@Parameter(in = ParameterIn.HEADER, description = "Admin's access token for authorization." ,required=true,schema=@Schema()) @RequestHeader(value="access_token", required=true) String accessToken
) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Showtime> showtimesShowtimeIdGet(@Parameter(in = ParameterIn.PATH, description = "ID of the showtime to retrieve details for.", required=true, schema=@Schema()) @PathVariable("showtime_id") String showtimeId
) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<Showtime>(objectMapper.readValue("{\n  \"date_time\" : \"2000-01-23T04:56:07.000+00:00\",\n  \"movie\" : {\n    \"upcomingRelease\" : false,\n    \"currentlyPlaying\" : true,\n    \"reviewScore\" : 8.9,\n    \"releaseDate\" : \"2023-07-21T00:00:00.000+00:00\",\n    \"director\" : \"Christopher Nolan\",\n    \"genre\" : \"Thriller\",\n    \"rating\" : \"R\",\n    \"length\" : \"3h5m\",\n    \"title\" : \"Oppenheimer\"\n  },\n  \"theater_box\" : {\n    \"box_number\" : 0,\n    \"reserved_seats\" : 1,\n    \"ticket_price\" : 5.962134,\n    \"total_seats\" : 6\n  },\n  \"id\" : \"id\"\n}", Showtime.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<Showtime>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<Showtime>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<Movie>> showtimesShowtimeIdMoviesGet(@Parameter(in = ParameterIn.PATH, description = "ID of the showtime to retrieve movies for.", required=true, schema=@Schema()) @PathVariable("showtime_id") String showtimeId
) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<Movie>>(objectMapper.readValue("[ {\n  \"upcomingRelease\" : false,\n  \"currentlyPlaying\" : true,\n  \"reviewScore\" : 8.9,\n  \"releaseDate\" : \"2023-07-21T00:00:00.000+00:00\",\n  \"director\" : \"Christopher Nolan\",\n  \"genre\" : \"Thriller\",\n  \"rating\" : \"R\",\n  \"length\" : \"3h5m\",\n  \"title\" : \"Oppenheimer\"\n}, {\n  \"upcomingRelease\" : false,\n  \"currentlyPlaying\" : true,\n  \"reviewScore\" : 8.9,\n  \"releaseDate\" : \"2023-07-21T00:00:00.000+00:00\",\n  \"director\" : \"Christopher Nolan\",\n  \"genre\" : \"Thriller\",\n  \"rating\" : \"R\",\n  \"length\" : \"3h5m\",\n  \"title\" : \"Oppenheimer\"\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<Movie>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<Movie>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<Void> showtimesShowtimeIdPut(@Parameter(in = ParameterIn.HEADER, description = "Admin's access token for authorization." ,required=true,schema=@Schema()) @RequestHeader(value="access_token", required=true) String accessToken
,@Parameter(in = ParameterIn.PATH, description = "ID of the showtime to update.", required=true, schema=@Schema()) @PathVariable("showtime_id") String showtimeId
,@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody Showtime body
) {
        String accept = request.getHeader("Accept");
        return new ResponseEntity<Void>(HttpStatus.NOT_IMPLEMENTED);
    }

}
