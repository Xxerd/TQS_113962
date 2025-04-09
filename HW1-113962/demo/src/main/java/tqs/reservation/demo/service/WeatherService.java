package tqs.reservation.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import tqs.reservation.demo.dto.WeatherForecastDTO;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class WeatherService {
    private static final int CACHE_REFRESH = 72000;
    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);
    private final String apiKey = "";
    private final String apiUrl = "https://api.openweathermap.org/data/2.5/forecast";
    @Autowired
    private final RestTemplate restTemplate;

    private final Map<String, List<WeatherForecastDTO>> cache = new HashMap<>();
    private int totalRequests = 0;
    private int cacheHits = 0;
    private int cacheMisses = 0;
    Logger log = LoggerFactory.getLogger(WeatherService.class);

    WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<WeatherForecastDTO> getWeatherForecast(double latitude, double longitude, LocalDate day) {
        incrementTotalRequests();
        String url = apiUrl + "?lat=" + latitude + "&lon=" + longitude
                + "&appid=" + apiKey + "&units=metric";
        String cacheKey = generateCacheKey(latitude, longitude, day);
        if (cache.containsKey(cacheKey)) {
            logger.info("Cache contains key, checking validity: {}", cacheKey);
            boolean isValid = cache.get(cacheKey).stream()
                    .noneMatch(
                            forecast -> forecast.getCreationTime().isBefore(Instant.now().minusSeconds(CACHE_REFRESH)));
            if (isValid) {
                logger.info("Cache hit for key: {}", cacheKey);
                incrementCacheHit();
                return cache.get(cacheKey);
            } else {
                logger.info("Cache expired for key: {}", cacheKey);
                cache.remove(cacheKey);
            }
            return cache.get(cacheKey);
        }
        String response = restTemplate.getForObject(url, String.class);
        if (response == null) {
            log.info("weather API returned null");
            throw new RuntimeException("Failed to fetch weather data");
        }
        log.info("weather API was called");
        incrementCacheMiss();

        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();

        List<WeatherForecastDTO> forecasts = new ArrayList<>();
        jsonObject.getAsJsonArray("list").forEach(element -> {
            JsonObject forecast = element.getAsJsonObject();

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(forecast.get("dt_txt").getAsString(), formatter);

            if (dateTime.toLocalDate().equals(day)) {
                double temperature = forecast.getAsJsonObject("main").get("temp").getAsDouble();
                String weatherDescription = forecast.getAsJsonArray("weather").get(0).getAsJsonObject()
                        .get("description").getAsString();
                int cloudPercentage = forecast.getAsJsonObject("clouds").get("all").getAsInt();
                double windSpeed = forecast.getAsJsonObject("wind").get("speed").getAsDouble();

                forecasts.add(
                        new WeatherForecastDTO(dateTime, temperature, weatherDescription, cloudPercentage, windSpeed));
            }

        });

        cache.put(cacheKey, forecasts);

        return forecasts;
    }

    private String generateCacheKey(double latitude, double longitude, LocalDate day) {
        return latitude + "," + longitude + "," + day.toString();
    }

    public void incrementCacheHit() {
        cacheHits++;
    }

    public void incrementCacheMiss() {
        cacheMisses++;
    }

    public void incrementTotalRequests() {
        totalRequests++;
    }

    public int getTotalRequests() {
        return totalRequests;
    }

    public int getCacheHits() {
        return cacheHits;
    }

    public int getCacheMisses() {
        return cacheMisses;
    }

}