package io.swagger.api;

import io.swagger.jpa.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reports")
public class ReportsController {

    @Autowired
    private MovieRepository movieRepository;

    @GetMapping("/summary")
    public ResponseEntity<Map<String, Object>> getSummaryReport(
            @RequestParam("startTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam("endTime") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        List<Object[]> results = movieRepository.findTotalRevenueByMovie(startTime, endTime);

        Map<String, Object> response = new HashMap<>();
        for (Object[] result : results) {
            String movieTitle = (String) result[0];
            Double totalRevenue = (Double) result[1];
            response.put(movieTitle, totalRevenue);
        }

        return ResponseEntity.ok(response);
    }
}