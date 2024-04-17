package io.swagger.api;

import java.math.BigDecimal;
import io.swagger.model.Movie;
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
public class MoviesApiController implements MoviesApi {

	private static final Logger log = LoggerFactory.getLogger(MoviesApiController.class);

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@org.springframework.beans.factory.annotation.Autowired
	public MoviesApiController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	public ResponseEntity<Movie> moviesDelete(
			@Parameter(in = ParameterIn.HEADER, description = "Admin's access token for authorization.", required = true, schema = @Schema()) @RequestHeader(value = "access_token", required = true) String accessToken,
			@NotNull @Parameter(in = ParameterIn.QUERY, description = "the title of the movie to delete", required = true, schema = @Schema()) @Valid @RequestParam(value = "title", required = true) String title) {
		String accept = request.getHeader("Accept");
		if (accept != null && accept.contains("application/json")) {
			try {
				return new ResponseEntity<Movie>(objectMapper.readValue(
						"{\n  \"upcomingRelease\" : false,\n  \"currentlyPlaying\" : true,\n  \"reviewScore\" : 8.9,\n  \"releaseDate\" : \"2023-07-21T00:00:00.000+00:00\",\n  \"director\" : \"Christopher Nolan\",\n  \"genre\" : \"Thriller\",\n  \"rating\" : \"R\",\n  \"length\" : \"3h5m\",\n  \"title\" : \"Oppenheimer\"\n}",
						Movie.class), HttpStatus.NOT_IMPLEMENTED);
			} catch (IOException e) {
				log.error("Couldn't serialize response for content type application/json", e);
				return new ResponseEntity<Movie>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<Movie>(HttpStatus.NOT_IMPLEMENTED);
	}

	public ResponseEntity<Movie> moviesGet(
			@NotNull @Parameter(in = ParameterIn.QUERY, description = "the title of the movie to get details about", required = true, schema = @Schema()) @Valid @RequestParam(value = "title", required = true) String title) {
		String accept = request.getHeader("Accept");
		if (accept != null && accept.contains("application/json")) {
			try {
				return new ResponseEntity<Movie>(objectMapper.readValue(
						"{\n  \"upcomingRelease\" : false,\n  \"currentlyPlaying\" : true,\n  \"reviewScore\" : 8.9,\n  \"releaseDate\" : \"2023-07-21T00:00:00.000+00:00\",\n  \"director\" : \"Christopher Nolan\",\n  \"genre\" : \"Thriller\",\n  \"rating\" : \"R\",\n  \"length\" : \"3h5m\",\n  \"title\" : \"Oppenheimer\"\n}",
						Movie.class), HttpStatus.NOT_IMPLEMENTED);
			} catch (IOException e) {
				log.error("Couldn't serialize response for content type application/json", e);
				return new ResponseEntity<Movie>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<Movie>(HttpStatus.NOT_IMPLEMENTED);
	}

	public ResponseEntity<Movie> moviesPost(
			@Parameter(in = ParameterIn.HEADER, description = "Admin's access token for authorization.", required = true, schema = @Schema()) @RequestHeader(value = "access_token", required = true) String accessToken,
			@Parameter(in = ParameterIn.DEFAULT, description = "", required = true, schema = @Schema()) @Valid @RequestBody Movie body) {
		String accept = request.getHeader("Accept");
		if (accept != null && accept.contains("application/json")) {
			try {
				return new ResponseEntity<Movie>(objectMapper.readValue(
						"{\n  \"upcomingRelease\" : false,\n  \"currentlyPlaying\" : true,\n  \"reviewScore\" : 8.9,\n  \"releaseDate\" : \"2023-07-21T00:00:00.000+00:00\",\n  \"director\" : \"Christopher Nolan\",\n  \"genre\" : \"Thriller\",\n  \"rating\" : \"R\",\n  \"length\" : \"3h5m\",\n  \"title\" : \"Oppenheimer\"\n}",
						Movie.class), HttpStatus.NOT_IMPLEMENTED);
			} catch (IOException e) {
				log.error("Couldn't serialize response for content type application/json", e);
				return new ResponseEntity<Movie>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<Movie>(HttpStatus.NOT_IMPLEMENTED);
	}

	public ResponseEntity<Movie> moviesPut(
			@Parameter(in = ParameterIn.HEADER, description = "Admin's access token for authorization.", required = true, schema = @Schema()) @RequestHeader(value = "access_token", required = true) String accessToken,
			@NotNull @Parameter(in = ParameterIn.QUERY, description = "the title of the movie to update", required = true, schema = @Schema()) @Valid @RequestParam(value = "title", required = true) String title,
			@Parameter(in = ParameterIn.QUERY, description = "the updated director", schema = @Schema()) @Valid @RequestParam(value = "director", required = false) String director,
			@Parameter(in = ParameterIn.QUERY, description = "the updated genre", schema = @Schema()) @Valid @RequestParam(value = "genre", required = false) String genre,
			@Parameter(in = ParameterIn.QUERY, description = "the updated rating", schema = @Schema()) @Valid @RequestParam(value = "rating", required = false) String rating,
			@Parameter(in = ParameterIn.QUERY, description = "the updated length", schema = @Schema()) @Valid @RequestParam(value = "length", required = false) String length,
			@Parameter(in = ParameterIn.QUERY, description = "the updated review score", schema = @Schema()) @Valid @RequestParam(value = "reviewScore", required = false) BigDecimal reviewScore,
			@Parameter(in = ParameterIn.QUERY, description = "the updated release date", schema = @Schema()) @Valid @RequestParam(value = "releaseDate", required = false) String releaseDate,
			@Parameter(in = ParameterIn.QUERY, description = "the updated value for if the movie is currently playing", schema = @Schema()) @Valid @RequestParam(value = "currentlyPlaying", required = false) Boolean currentlyPlaying,
			@Parameter(in = ParameterIn.QUERY, description = "the updated value for if the move is an upcoming release", schema = @Schema()) @Valid @RequestParam(value = "upcomingRelease", required = false) Boolean upcomingRelease) {
		String accept = request.getHeader("Accept");
		if (accept != null && accept.contains("application/json")) {
			try {
				return new ResponseEntity<Movie>(objectMapper.readValue(
						"{\n  \"upcomingRelease\" : false,\n  \"currentlyPlaying\" : true,\n  \"reviewScore\" : 8.9,\n  \"releaseDate\" : \"2023-07-21T00:00:00.000+00:00\",\n  \"director\" : \"Christopher Nolan\",\n  \"genre\" : \"Thriller\",\n  \"rating\" : \"R\",\n  \"length\" : \"3h5m\",\n  \"title\" : \"Oppenheimer\"\n}",
						Movie.class), HttpStatus.NOT_IMPLEMENTED);
			} catch (IOException e) {
				log.error("Couldn't serialize response for content type application/json", e);
				return new ResponseEntity<Movie>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<Movie>(HttpStatus.NOT_IMPLEMENTED);
	}

}
