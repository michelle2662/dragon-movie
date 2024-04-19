package io.swagger.api;

import java.math.BigDecimal;
import java.net.URI;
import java.util.Optional;

import io.swagger.jpa.MovieRepository;
import io.swagger.model.Movie;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.threeten.bp.LocalDate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-19T09:54:05.273201+01:00[Europe/London]")
@RestController
public class MoviesApiController implements MoviesApi {

	private static final Logger log = LoggerFactory.getLogger(MoviesApiController.class);

	private final HttpServletRequest request;
	
	private static final String API_PATH = "apis/MORGANMAZER/dragon/1.0/";
	
	@Autowired
	private MovieRepository movieRepository;

	@Autowired
	public MoviesApiController(HttpServletRequest request) {
		this.request = request;
	}

	public ResponseEntity<Movie> moviesIdDelete(
			@Parameter(in = ParameterIn.HEADER, description = "Admin's access token for authorization.", required = true, schema = @Schema()) @RequestHeader(value = "access_token", required = true) String accessToken,
			@Parameter(in = ParameterIn.PATH, description = "the id of the movie to delete", required = true, schema = @Schema()) @PathVariable("id") Integer id) {
		log.info("DELETE /movies id" + id);
		
		String token = request.getHeader("access_token");
		
		if (token != null && !token.isEmpty()) { // TODO: actual token verification
			// delete the given movie
			movieRepository.deleteById((long) id);
			
			return ResponseEntity.ok().build();
		} else {
			return new ResponseEntity<Movie>(HttpStatus.FORBIDDEN);
		}
	}

	public ResponseEntity<Movie> moviesIdGet(
			@Parameter(in = ParameterIn.PATH, description = "the id of the movie to get details about", required = true, schema = @Schema()) @PathVariable("id") Integer id) {
		log.info("GET /movies id " + id);
		
		Optional<Movie> optionalMovie = movieRepository.findById((long) id);
		
		if (optionalMovie.isPresent()) {
			Movie movie = optionalMovie.get();
			return ResponseEntity.ok().body(movie);
		} else {
			return ResponseEntity.ok().build();
		}
	}

	public ResponseEntity<Movie> moviesIdPut(
			@Parameter(in = ParameterIn.HEADER, description = "Admin's access token for authorization.", required = true, schema = @Schema()) @RequestHeader(value = "access_token", required = true) String accessToken,
			@Parameter(in = ParameterIn.PATH, description = "the id of the movie to update", required = true, schema = @Schema()) @PathVariable("id") Integer id,
			@Parameter(in = ParameterIn.QUERY, description = "the updated movie title", schema = @Schema()) @Valid @RequestParam(value = "title", required = false) String title,
			@Parameter(in = ParameterIn.QUERY, description = "the updated director", schema = @Schema()) @Valid @RequestParam(value = "director", required = false) String director,
			@Parameter(in = ParameterIn.QUERY, description = "the updated genre", schema = @Schema()) @Valid @RequestParam(value = "genre", required = false) String genre,
			@Parameter(in = ParameterIn.QUERY, description = "the updated rating", schema = @Schema()) @Valid @RequestParam(value = "rating", required = false) String rating,
			@Parameter(in = ParameterIn.QUERY, description = "the updated length", schema = @Schema()) @Valid @RequestParam(value = "length", required = false) String length,
			@Parameter(in = ParameterIn.QUERY, description = "the updated review score", schema = @Schema()) @Valid @RequestParam(value = "reviewScore", required = false) BigDecimal reviewScore,
			@Parameter(in = ParameterIn.QUERY, description = "the updated release date", schema = @Schema()) @Valid @RequestParam(value = "releaseDate", required = false) String releaseDate,
			@Parameter(in = ParameterIn.QUERY, description = "the updated value for if the movie is currently playing", schema = @Schema()) @Valid @RequestParam(value = "currentlyPlaying", required = false) Boolean currentlyPlaying,
			@Parameter(in = ParameterIn.QUERY, description = "the updated value for if the move is an upcoming release", schema = @Schema()) @Valid @RequestParam(value = "upcomingRelease", required = false) Boolean upcomingRelease) {
		log.info("PUT /movies id" + id);
		
		String token = request.getHeader("access_token");
		
		if (token != null && !token.isEmpty()) { // TODO: actual token verification
			// get the movie to update
			Optional<Movie> optionalMovie = movieRepository.findById((long) id);
			if (optionalMovie.isPresent()) {
				Movie toUpdate = optionalMovie.get();
				
				// update the provided attributes
				if (title != null) {
					toUpdate.setTitle(title);
				}
				if (director != null) {
					toUpdate.setDirector(director);
				}
				if (genre != null) {
					toUpdate.setGenre(genre);
				}
				if (rating != null) {
					toUpdate.setRating(rating);
				}
				if (length != null) {
					toUpdate.setLength(length);
				}
				if (reviewScore != null) {
					toUpdate.setReviewScore(reviewScore);
				}
				if (releaseDate != null) {
					toUpdate.setReleaseDate(LocalDate.parse(releaseDate));
				}
				if (currentlyPlaying != null) {
					toUpdate.setCurrentlyPlaying(currentlyPlaying);
				}
				if (upcomingRelease != null) {
					toUpdate.setUpcomingRelease(upcomingRelease);
				}
				
				// save the updated movie
				movieRepository.save(toUpdate);
				
				// return the updated movie
				return ResponseEntity.ok().body(toUpdate);
			} else {
				return ResponseEntity.ok().build();
			}
		} else {
			return new ResponseEntity<Movie>(HttpStatus.FORBIDDEN);
		}
	}

	public ResponseEntity<Movie> moviesPost(
			@Parameter(in = ParameterIn.HEADER, description = "Admin's access token for authorization.", required = true, schema = @Schema()) @RequestHeader(value = "access_token", required = true) String accessToken,
			@Parameter(in = ParameterIn.DEFAULT, description = "", required = true, schema = @Schema()) @Valid @RequestBody Movie body) {
		log.info("POST /movies");
		
		String token = request.getHeader("access_token");
		
		if (token != null && !token.isEmpty()) { // TODO: actual token verification
			// save the movie
			movieRepository.save(body);
			
			// build URI for newly-created movie
			String host = System.getProperty("host", "localhost");
			String port = System.getProperty("port", "8080");
			String baseUrl = "http://{host}:{port}/" + API_PATH + "movie/";
			
			URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl).host(host).port(port).path("{id}").build(body.getId());
			
			return ResponseEntity.created(uri).body(body);
		} else {
			return new ResponseEntity<Movie>(HttpStatus.FORBIDDEN);
		}
	}

}
