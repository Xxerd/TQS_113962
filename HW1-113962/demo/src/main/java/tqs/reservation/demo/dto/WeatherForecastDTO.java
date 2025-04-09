package tqs.reservation.demo.dto;

import java.time.Instant;
import java.time.LocalDateTime;

public class WeatherForecastDTO {
    private LocalDateTime dateTime;
    private double temperature;
    private String weatherDescription;
    private int cloudPercentage;
    private double windSpeed;
    private Instant creationTime = Instant.now();

    public WeatherForecastDTO() {
    }

    public WeatherForecastDTO(LocalDateTime dateTime, double temperature, String weatherDescription,
            int cloudPercentage, double windSpeed) {
        this.dateTime = dateTime;
        this.temperature = temperature;
        this.weatherDescription = weatherDescription;
        this.cloudPercentage = cloudPercentage;
        this.windSpeed = windSpeed;
        this.creationTime = Instant.now();
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public double getTemperature() {
        return temperature;
    }

    public void setTemperature(double temperature) {
        this.temperature = temperature;
    }

    public String getWeatherDescription() {
        return weatherDescription;
    }

    public void setWeatherDescription(String weatherDescription) {
        this.weatherDescription = weatherDescription;
    }

    public int getCloudPercentage() {
        return cloudPercentage;
    }

    public void setCloudPercentage(int cloudPercentage) {
        this.cloudPercentage = cloudPercentage;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Instant getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Instant creationTime) {
        this.creationTime = creationTime;
    }

    @Override
    public String toString() {
        return "WeatherForecastDTO{" +
                "dateTime=" + dateTime +
                ", temperature=" + temperature +
                ", weatherDescription='" + weatherDescription + '\'' +
                ", cloudPercentage=" + cloudPercentage +
                ", windSpeed=" + windSpeed +
                '}';
    }

}
