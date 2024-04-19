package io.swagger.api;

import io.swagger.jpa.MembershipRepository;
import io.swagger.model.Membership;
import io.swagger.model.MembershipRequestBody;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Schema;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.net.URI;
import java.util.Optional;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-19T09:54:05.273201+01:00[Europe/London]")
@RestController
public class MembershipApiController implements MembershipApi {

	private static final Logger log = LoggerFactory.getLogger(MembershipApiController.class);
	
	@Autowired
	private MembershipRepository membershipRepository;
	
	private static final String API_PATH = "apis/MORGANMAZER/dragon/1.0/";

	@Autowired
	public MembershipApiController() {
	}

	public ResponseEntity<Membership> membershipIdGet(
			@Parameter(in = ParameterIn.PATH, description = "the id of the membership", required = true, schema = @Schema()) @PathVariable("id") Integer id) {
		log.info("GET /membership id " + id);
		
		// retrieve membership
		Optional<Membership> optionalMembership = membershipRepository.findById((long) id);
		
		if (optionalMembership.isPresent()) {
			Membership membership = optionalMembership.get();
			return ResponseEntity.ok().body(membership);
		} else {
			return ResponseEntity.ok().build();
		}
	}

	public ResponseEntity<Membership> membershipPost(
			@Parameter(in = ParameterIn.DEFAULT, description = "", required = true, schema = @Schema()) @Valid @RequestBody MembershipRequestBody body) {
		log.info("POST /membership " + body.toString());
		
		// convert MembershipRequestBody to Membership
		Membership membership = new Membership();
		membership.setFirstName(body.getFirstName());
		membership.setLastName(body.getLastName());
		membership.setEmail(body.getEmail());
		
		// save the membership
		membershipRepository.save(membership);
		
		// build URI for newly created membership
		String host = System.getProperty("host", "localhost");
		String port = System.getProperty("port", "8080");
		String baseUrl = "http://{host}:{port}/" + API_PATH + "membership/";
		
		URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl).host(host).port(port).path("{id}").build(membership.getId());
		
		return ResponseEntity.created(uri).body(membership); 
	}

}
