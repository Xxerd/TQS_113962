package tqs.reservation.demo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tqs.reservation.demo.dto.WeatherForecastDTO;
import tqs.reservation.demo.service.WeatherService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/apiV1/weather")
public class WeatherController {
    @Autowired
    private WeatherService weatherService;

    @GetMapping("/forecast")
    @Operation(summary = "Get weather forecast for a specific location and date")
    public ResponseEntity<List<WeatherForecastDTO>> getWeatherForecast(@RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam LocalDate day) {
        if (day.isBefore(LocalDate.now()) || day.isAfter(LocalDate.now().plusDays(4))) {
            return ResponseEntity.badRequest().body(null);
        }
        if (latitude < -90 || latitude > 90 || longitude < -180 || longitude > 180) {
            return ResponseEntity.badRequest().body(null);
        }
        List<WeatherForecastDTO> forecast = weatherService.getWeatherForecast(latitude, longitude, day);

        if (forecast == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(forecast);
    }

    @GetMapping("/cache/stats")
    @Operation(summary = "Get cache statistics")
    public ResponseEntity<String> getCacheStats() {
        int totalRequests = weatherService.getTotalRequests();
        int cacheHits = weatherService.getCacheHits();
        int cacheMisses = weatherService.getCacheMisses();

        String stats = String.format("Total Requests: %d, Cache Hits: %d, Cache Misses: %d", totalRequests, cacheHits,
                cacheMisses);
        return ResponseEntity.ok(stats);
    }
}