package tqs.reservation.demo.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import tqs.reservation.demo.dto.WeatherForecastDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@Service
public class WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);
    private final String apiKey = "";
    private final String apiUrl = "https://api.openweathermap.org/data/2.5/forecast";
    private final RestTemplate restTemplate;
    Logger log = LoggerFactory.getLogger(WeatherService.class);

    WeatherService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Cacheable(value = "weatherCache", key = "#latitude + ',' + #longitude + ',' + #day", unless = "#result == null || #result.isEmpty()")
    public List<WeatherForecastDTO> getWeatherForecast(double latitude, double longitude, LocalDate day) {
        String url = apiUrl + "?lat=" + latitude + "&lon=" + longitude + "&ctn=" + System.currentTimeMillis() + "&appid=" + apiKey + "&units=metric";

        String response = restTemplate.getForObject(url, String.class);

        logger.info("Weather API response: {}", response);
        if (response == null) {
            throw new RuntimeException("Failed to fetch weather data");
        }

        JsonObject jsonObject = JsonParser.parseString(response).getAsJsonObject();

        List<WeatherForecastDTO> forecasts = new ArrayList<>();
        int timestamp = jsonObject.get("cnt").getAsInt();
        jsonObject.getAsJsonArray("list").forEach(element -> {
            JsonObject forecast = element.getAsJsonObject();

            LocalDateTime dateTime = LocalDateTime.parse(forecast.get("dt_txt").getAsString());

            if (dateTime.toLocalDate().equals(day)) {
                double temperature = forecast.getAsJsonObject("main").get("temp").getAsDouble();
                String weatherDescription = forecast.getAsJsonArray("weather").get(0).getAsJsonObject()
                        .get("description").getAsString();
                int cloudPercentage = forecast.getAsJsonObject("clouds").get("all").getAsInt();
                double windSpeed = forecast.getAsJsonObject("wind").get("speed").getAsDouble();

                forecasts.add(
                        new WeatherForecastDTO(dateTime, temperature, weatherDescription, cloudPercentage, windSpeed,
                                timestamp));
            }

        });

        return forecasts;
    }

}