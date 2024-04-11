package io.swagger.api;

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
public class TheaterBoxesApiController implements TheaterBoxesApi {

    private static final Logger log = LoggerFactory.getLogger(TheaterBoxesApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @org.springframework.beans.factory.annotation.Autowired
    public TheaterBoxesApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<TheaterBox> theaterBoxesBoxNumberGet(@Parameter(in = ParameterIn.PATH, description = "Number of the theater box to retrieve details for.", required=true, schema=@Schema()) @PathVariable("box_number") Integer boxNumber
) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<TheaterBox>(objectMapper.readValue("{\n  \"box_number\" : 0,\n  \"reserved_seats\" : 1,\n  \"ticket_price\" : 5.962134,\n  \"total_seats\" : 6\n}", TheaterBox.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<TheaterBox>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<TheaterBox>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<TheaterBox>> theaterBoxesGet() {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<TheaterBox>>(objectMapper.readValue("[ {\n  \"box_number\" : 0,\n  \"reserved_seats\" : 1,\n  \"ticket_price\" : 5.962134,\n  \"total_seats\" : 6\n}, {\n  \"box_number\" : 0,\n  \"reserved_seats\" : 1,\n  \"ticket_price\" : 5.962134,\n  \"total_seats\" : 6\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<TheaterBox>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<TheaterBox>>(HttpStatus.NOT_IMPLEMENTED);
    }

    public ResponseEntity<List<TheaterBox>> theaterBoxesPost(@Parameter(in = ParameterIn.HEADER, description = "Admin's access token for authorization." ,required=true,schema=@Schema()) @RequestHeader(value="access_token", required=true) String accessToken
,@Parameter(in = ParameterIn.DEFAULT, description = "", required=true, schema=@Schema()) @Valid @RequestBody TheaterBox body
) {
        String accept = request.getHeader("Accept");
        if (accept != null && accept.contains("application/json")) {
            try {
                return new ResponseEntity<List<TheaterBox>>(objectMapper.readValue("[ {\n  \"box_number\" : 0,\n  \"reserved_seats\" : 1,\n  \"ticket_price\" : 5.962134,\n  \"total_seats\" : 6\n}, {\n  \"box_number\" : 0,\n  \"reserved_seats\" : 1,\n  \"ticket_price\" : 5.962134,\n  \"total_seats\" : 6\n} ]", List.class), HttpStatus.NOT_IMPLEMENTED);
            } catch (IOException e) {
                log.error("Couldn't serialize response for content type application/json", e);
                return new ResponseEntity<List<TheaterBox>>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        return new ResponseEntity<List<TheaterBox>>(HttpStatus.NOT_IMPLEMENTED);
    }

}
