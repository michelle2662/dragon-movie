/**
 * NOTE: This class is auto generated by the swagger code generator program (3.0.54).
 * https://github.com/swagger-api/swagger-codegen
 * Do not edit the class manually.
 */
package io.swagger.api;

import java.math.BigDecimal;
import org.threeten.bp.LocalDateTime;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.CookieValue;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.util.List;
import java.util.Map;

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.SpringCodegen", date = "2024-04-27T18:43:33.268443-04:00[America/New_York]")
@Validated
public interface ReportsApi {

    @Operation(summary = "Get summary report", description = "Allows an admin to retrieve a summary report of total revenue per movie.", security = {
        @SecurityRequirement(name = "bearerAuth")    }, tags={  })
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Summary report retrieved successfully.", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = BigDecimal.class)))),
        
        @ApiResponse(responseCode = "403", description = "Forbidden. Only admins are allowed to perform this action.") })
    @RequestMapping(value = "/reports/summary",
        produces = { "application/json" }, 
        method = RequestMethod.GET)
    ResponseEntity<Map<String, BigDecimal>> reportsSummaryGet(@NotNull @Parameter(in = ParameterIn.QUERY, description = "Start date of the report period." ,required=true,schema=@Schema()) @Valid @RequestParam(value = "start_date_time", required = true) LocalDateTime startDateTime
, @NotNull @Parameter(in = ParameterIn.QUERY, description = "End date of the report period." ,required=true,schema=@Schema()) @Valid @RequestParam(value = "end_date_time", required = true) LocalDateTime endDateTime
);

}

