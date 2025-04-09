package tqs.reservation.demo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import tqs.reservation.demo.model.Restaurant;
import tqs.reservation.demo.repository.RestaurantRepository;
import tqs.reservation.demo.service.RestaurantService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.client.RestTemplate;
import tqs.reservation.demo.dto.WeatherForecastDTO;
import tqs.reservation.demo.service.WeatherService;

public class WeatherServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private WeatherService weatherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCacheHit() {
        double latitude = 40.0;
        double longitude = -8.0;
        LocalDate day = LocalDate.now();

        weatherService.cache.put(weatherService.generateCacheKey(latitude, longitude, day),
                new ArrayList<WeatherForecastDTO>());

        weatherService.getWeatherForecast(latitude, longitude, day);

        assertEquals(1, weatherService.getCacheHits());
        assertEquals(0, weatherService.getCacheMisses());
        assertEquals(1, weatherService.getTotalRequests());
    }

    @Test
    void testCacheMiss() {
        double latitude = 40.0;
        double longitude = -8.0;
        LocalDate day = LocalDate.now();

        String response = "{ \"list\": [ { \"dt_txt\": \"" + day.toString()
                + " 12:00:00\", \"main\": { \"temp\": 20.0 }, \"weather\": [ { \"description\": \"clear sky\" } ], \"clouds\": { \"all\": 10 }, \"wind\": { \"speed\": 5.0 } } ] }";

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(response);

        weatherService.getWeatherForecast(latitude, longitude, day); // Should miss cache

        assertEquals(0, weatherService.getCacheHits());
        assertEquals(1, weatherService.getCacheMisses());
    }

    @Test
    void testCacheExpiration() {
        double latitude = 40.0;
        double longitude = -8.0;
        LocalDate day = LocalDate.now();
        WeatherService.CACHE_REFRESH = 1;

        String response = "{ \"list\": [ { \"dt_txt\": \"" + day.toString()
                + " 12:00:00\", \"main\": { \"temp\": 20.0 }, \"weather\": [ { \"description\": \"clear sky\" } ], \"clouds\": { \"all\": 10 }, \"wind\": { \"speed\": 5.0 } } ] }";

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(response);

        weatherService.getWeatherForecast(latitude, longitude, day); // Populate cache
        weatherService.getWeatherForecast(latitude, longitude, day); // Should hit cache

        try {
            Thread.sleep(WeatherService.CACHE_REFRESH * 1000L + 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        weatherService.getWeatherForecast(latitude, longitude, day);

        assertEquals(3, weatherService.getTotalRequests());
        assertEquals(1, weatherService.getCacheHits());
        assertEquals(2, weatherService.getCacheMisses());

    }
}
