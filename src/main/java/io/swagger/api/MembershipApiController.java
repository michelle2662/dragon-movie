package io.swagger.api;

import io.swagger.model.Membership;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;
import javax.validation.Valid;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-19T09:54:05.273201+01:00[Europe/London]")
@RestController
public class MembershipApiController implements MembershipApi {

	private static final Logger log = LoggerFactory.getLogger(MembershipApiController.class);

	private final ObjectMapper objectMapper;

	private final HttpServletRequest request;

	@org.springframework.beans.factory.annotation.Autowired
	public MembershipApiController(ObjectMapper objectMapper, HttpServletRequest request) {
		this.objectMapper = objectMapper;
		this.request = request;
	}

	public ResponseEntity<Membership> membershipIdGet(
			@Parameter(in = ParameterIn.PATH, description = "the id of the membership", required = true, schema = @Schema()) @PathVariable("id") Integer id) {
		String accept = request.getHeader("Accept");
		if (accept != null && accept.contains("application/json")) {
			try {
				return new ResponseEntity<Membership>(objectMapper.readValue(
						"{\n  \"firstName\" : \"John\",\n  \"lastName\" : \"Smith\",\n  \"id\" : 34,\n  \"email\" : \"john.smith@email.com\"\n}",
						Membership.class), HttpStatus.NOT_IMPLEMENTED);
			} catch (IOException e) {
				log.error("Couldn't serialize response for content type application/json", e);
				return new ResponseEntity<Membership>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<Membership>(HttpStatus.NOT_IMPLEMENTED);
	}

	public ResponseEntity<Membership> membershipPost(
			@Parameter(in = ParameterIn.DEFAULT, description = "", required = true, schema = @Schema()) @Valid @RequestBody Membership body) {
		String accept = request.getHeader("Accept");
		if (accept != null && accept.contains("application/json")) {
			try {
				return new ResponseEntity<Membership>(objectMapper.readValue(
						"{\n  \"firstName\" : \"John\",\n  \"lastName\" : \"Smith\",\n  \"id\" : 34,\n  \"email\" : \"john.smith@email.com\"\n}",
						Membership.class), HttpStatus.NOT_IMPLEMENTED);
			} catch (IOException e) {
				log.error("Couldn't serialize response for content type application/json", e);
				return new ResponseEntity<Membership>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}

		return new ResponseEntity<Membership>(HttpStatus.NOT_IMPLEMENTED);
	}

}
