package io.swagger.api;

import io.swagger.jpa.MovieRepository;
import io.swagger.jpa.ShowtimeRepository;
import io.swagger.model.Movie;
import io.swagger.model.Showtime;
import io.swagger.model.ShowtimeRequestBody;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
public class ShowtimesApiController implements ShowtimesApi {

    private static final Logger log = LoggerFactory.getLogger(ShowtimesApiController.class);

    private final HttpServletRequest request;

    private static final String API_PATH = "apis/MORGANMAZER/dragon/2.0/showtimes/";

    @Autowired
    private ShowtimeRepository showtimeRepository;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    public ShowtimesApiController(HttpServletRequest request) {
        this.request = request;
    }

    public ResponseEntity<List<Showtime>> showtimesGet() {
        log.info("GET /showtimes");

        List<Showtime> showtimes = showtimeRepository.findAll();
        return ResponseEntity.ok().body(showtimes);
    }

    public ResponseEntity<Void> showtimesPost(
        @Parameter(in = ParameterIn.HEADER, description = "Admin's access token for authorization.", required = true, schema = @Schema()) @RequestHeader(value = "access_token", required = true) String accessToken,
        @Parameter(in = ParameterIn.DEFAULT, description = "", required = true, schema = @Schema()) @Valid @RequestBody ShowtimeRequestBody body) {
        
        log.info("POST /showtimes");
        if (accessToken == null || accessToken.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
        Optional<Movie> movie = movieRepository.findById(Long.valueOf(body.getMovieId()));
        if (movie.isPresent()) {
            Showtime showtime = new Showtime();
            showtime.setDateTime(body.getDateTime());
            showtime.setMovie(movie.get());
            showtime.setTheaterBoxId(body.getTheaterBoxId());

            Showtime createdShowtime = showtimeRepository.save(showtime);
            URI location = UriComponentsBuilder.fromPath("apis/MORGANMAZER/dragon/2.0/showtimes/" + createdShowtime.getId()).build().toUri();
            return ResponseEntity.created(location).build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    public ResponseEntity<Void> showtimesShowtimeIdDelete(
            @Parameter(in = ParameterIn.PATH, description = "ID of the showtime to delete.", required = true, schema = @Schema()) @PathVariable("showtime_id") Long showtimeId,
            @Parameter(in = ParameterIn.HEADER, description = "Admin's access token for authorization.", required = true, schema = @Schema()) @RequestHeader(value = "access_token", required = true) String accessToken) {

        log.info("DELETE /showtimes/{}", showtimeId);

        String token = request.getHeader("access_token");

        if (token != null && !token.isEmpty()) { // TODO: actual token verification
            Optional<Showtime> optionalShowtime = showtimeRepository.findById(showtimeId);
            if (optionalShowtime.isPresent()) {
                showtimeRepository.delete(optionalShowtime.get());
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    public ResponseEntity<Showtime> showtimesShowtimeIdGet(
            @Parameter(in = ParameterIn.PATH, description = "ID of the showtime to retrieve details for.", required = true, schema = @Schema()) @PathVariable("showtime_id") Long showtimeId) {

        log.info("GET /showtimes/{}", showtimeId);

        Optional<Showtime> optionalShowtime = showtimeRepository.findById(showtimeId);
        return optionalShowtime.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    public ResponseEntity<List<Movie>> showtimesShowtimeIdMoviesGet(
            @Parameter(in = ParameterIn.PATH, description = "ID of the showtime to retrieve movies for.", required = true, schema = @Schema()) @PathVariable("showtime_id") Long showtimeId) {
                log.info("GET /showtimes/{}/movies", showtimeId);

                List<Movie> movies = showtimeRepository.findMoviesByShowtimeId(showtimeId);
                return ResponseEntity.ok(movies);
    }

    public ResponseEntity<Void> showtimesShowtimeIdPut(
            @Parameter(in = ParameterIn.HEADER, description = "Admin's access token for authorization.", required = true, schema = @Schema()) @RequestHeader(value = "access_token", required = true) String accessToken,
            @Parameter(in = ParameterIn.PATH, description = "ID of the showtime to update.", required = true, schema = @Schema()) @PathVariable("showtime_id") Long showtimeId,
            @Parameter(in = ParameterIn.DEFAULT, description = "", required = true, schema = @Schema()) @Valid @RequestBody ShowtimeRequestBody body) {

        log.info("PUT /showtimes/{}", showtimeId);

        String token = request.getHeader("access_token");
        
        if (token != null && !token.isEmpty()) { // TODO: actual token verification
            Optional<Showtime> optionalShowtime = showtimeRepository.findById(showtimeId);
            Optional<Movie> movie = movieRepository.findById(Long.valueOf(body.getMovieId()));
            if (optionalShowtime.isPresent()) {
                Showtime showtime = optionalShowtime.get();
                showtime.setDateTime(body.getDateTime());
                showtime.setMovie(movie.get());
                showtime.setTheaterBoxId(body.getTheaterBoxId());

                showtimeRepository.save(showtime);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } else {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}