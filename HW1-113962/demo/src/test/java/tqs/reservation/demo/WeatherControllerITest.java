package tqs.reservation.demo;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import tqs.reservation.demo.service.WeatherService;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
public class WeatherControllerITest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WeatherService weatherService;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    public void setUp() {
        weatherService.clearCache(); // Clear the cache before each test
    }

    @AfterEach
    public void tearDown() {
        weatherService.clearCache(); // Clear the cache after each test
    }

    @Test
    void testGetWeatherForecast() throws Exception {
        double latitude = 40.0;
        double longitude = -8.0;
        String day = "2025-04-10";

        mockMvc.perform(get("/apiV1/weather/forecast")
                .param("latitude", String.valueOf(latitude))
                .param("longitude", String.valueOf(longitude))
                .param("day", day))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThan(0))))
                .andExpect(jsonPath("$[0].temperature", notNullValue()));
    }

    @Test
    void testGetCacheStats() throws Exception {
        weatherService.incrementTotalRequests();
        weatherService.incrementCacheHit();
        weatherService.incrementCacheMiss();

        mockMvc.perform(get("/apiV1/weather/cache/stats"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Total Requests: 1, Cache Hits: 1, Cache Misses: 1")));

    }

    @Test
    void testGetCacheStats_EmptyCache() throws Exception {
        mockMvc.perform(get("/apiV1/weather/cache/stats"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Total Requests: 0")))
                .andExpect(content().string(containsString("Cache Hits: 0")))
                .andExpect(content().string(containsString("Cache Misses: 0")));
    }

    @Test
    void testMissingLatitudeParameter() throws Exception {
        mockMvc.perform(get("/apiV1/weather/forecast")
                .param("longitude", "-8.0")
                .param("day", "2025-04-10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testMissingLongitudeParameter() throws Exception {
        mockMvc.perform(get("/apiV1/weather/forecast")
                .param("latitude", "40.0")
                .param("day", "2025-04-10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testMissingDateParameter() throws Exception {
        mockMvc.perform(get("/apiV1/weather/forecast")
                .param("latitude", "40.0")
                .param("longitude", "-8.0"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testInvalidLatitudeParameter() throws Exception {
        mockMvc.perform(get("/apiV1/weather/forecast")
                .param("latitude", "invalid")
                .param("longitude", "-8.0")
                .param("day", "2025-04-10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testInvalidLongitudeParameter() throws Exception {
        mockMvc.perform(get("/apiV1/weather/forecast")
                .param("latitude", "40.0")
                .param("longitude", "invalid")
                .param("day", "2025-04-10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testInvalidDateParameter() throws Exception {
        mockMvc.perform(get("/apiV1/weather/forecast")
                .param("latitude", "40.0")
                .param("longitude", "-8.0")
                .param("day", "invalid-date"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testDateFarInTheFuture() throws Exception {
        mockMvc.perform(get("/apiV1/weather/forecast")
                .param("latitude", "40.0")
                .param("longitude", "-8.0")
                .param("day", "2100-01-01"))
                .andExpect(status().isBadRequest()); // Assuming no data for far future dates
    }

    @Test
    void testDateFarInThePast() throws Exception {
        mockMvc.perform(get("/apiV1/weather/forecast")
                .param("latitude", "40.0")
                .param("longitude", "-8.0")
                .param("day", "1900-01-01"))
                .andExpect(status().isBadRequest()); // Assuming no data for far past dates
    }

}