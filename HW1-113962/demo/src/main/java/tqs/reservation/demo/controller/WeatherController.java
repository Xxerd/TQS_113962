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

@RestController
@RequestMapping("/apiV1/weather")
public class WeatherController {
    @Autowired
    private WeatherService weatherService;

    @GetMapping("/forecast")
    public ResponseEntity<List<WeatherForecastDTO>> getWeatherForecast(@RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam LocalDate day) {
        List<WeatherForecastDTO> forecast = weatherService.getWeatherForecast(latitude, longitude, day);
        if (forecast == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(forecast);
    }
}