package io.swagger.api;

import io.swagger.jpa.TheaterBoxRepository;
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
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-11T20:35:28.031354+01:00[Europe/London]")
@RestController
public class TheaterBoxesApiController implements TheaterBoxesApi {

	private static final Logger log = LoggerFactory.getLogger(TheaterBoxesApiController.class);

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@Autowired
	private TheaterBoxRepository theaterBoxRepository;

	@org.springframework.beans.factory.annotation.Autowired
	public TheaterBoxesApiController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	public ResponseEntity<TheaterBox> theaterBoxesBoxNumberGet(
			@Parameter(in = ParameterIn.PATH, description = "Number of the theater box to retrieve details for.", required = true, schema = @Schema()) @PathVariable("box_number") Integer boxNumber) {

			try {
				TheaterBox theaterBox = theaterBoxRepository.findByBoxNumber(boxNumber);
				if (theaterBox != null) {
					return new ResponseEntity<>(theaterBox, HttpStatus.OK);
				} else {
					return new ResponseEntity<>(HttpStatus.NOT_FOUND);
				}
			} catch (Exception e) {
				log.error("Error retrieving theater box details", e);
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}

	}

	public ResponseEntity<List<TheaterBox>> theaterBoxesGet() {

			try {
				List<TheaterBox> theaterBoxes = theaterBoxRepository.findAll();
				return new ResponseEntity<>(theaterBoxes, HttpStatus.OK);
			} catch (Exception e) {
				log.error("Error retrieving theater boxes", e);
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}

	}


	public ResponseEntity<List<TheaterBox>> theaterBoxesPost(
			@Parameter(in = ParameterIn.HEADER, description = "Admin's access token for authorization.", required = true, schema = @Schema()) @RequestHeader(value = "access_token", required = true) String accessToken,
			@Parameter(in = ParameterIn.DEFAULT, description = "", required = true, schema = @Schema()) @Valid @RequestBody TheaterBox body
			) {

			try {


				// Save the new theater box to the database
				TheaterBox savedTheaterBox = theaterBoxRepository.save(body);

				// Return the newly created theater box in the response
				List<TheaterBox> theaterBoxList = Collections.singletonList(savedTheaterBox);
				return new ResponseEntity<>(theaterBoxList, HttpStatus.CREATED);
			} catch (Exception e) {
				log.error("Error creating new theater box", e);
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}

	}

}
