package io.swagger.api;

import java.math.BigDecimal;
import org.threeten.bp.LocalDateTime;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;

import io.swagger.jpa.MovieRepository;
import javax.validation.Valid;
import javax.validation.constraints.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-27T18:43:33.268443-04:00[America/New_York]")
@RestController
public class ReportsApiController implements ReportsApi {

    private static final Logger log = LoggerFactory.getLogger(ReportsApiController.class);

    private final ObjectMapper objectMapper;

    private final HttpServletRequest request;

    @Autowired
    private MovieRepository movieRepository;

    @org.springframework.beans.factory.annotation.Autowired
    public ReportsApiController(ObjectMapper objectMapper, HttpServletRequest request) {
        this.objectMapper = objectMapper;
        this.request = request;
    }

    public ResponseEntity<Map<String, BigDecimal>> reportsSummaryGet(
            @NotNull @Parameter(in = ParameterIn.QUERY, description = "Start date of the report period.", required = true, schema = @Schema()) @Valid @RequestParam(value = "start_date_time", required = true) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDateTime,
        List<Object[]> results = movieRepository.findTotalRevenueByMovie(startDateTime, endDateTime);
        Map<String, BigDecimal> response = new HashMap<>();

        for (Object[] result : results) {
            String movieTitle = (String) result[0];
            BigDecimal totalRevenue = BigDecimal.valueOf((Double) result[1]);
            response.put(movieTitle, totalRevenue);
        }

        return ResponseEntity.ok(response);
    }

}
