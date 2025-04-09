package tqs.reservation.demo.model;

import java.time.LocalDate;

import io.micrometer.common.lang.NonNull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String description;
    @Column(nullable = false)
    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be positive")
    private double price;
    @NotNull
    private LocalDate date;
    @NotNull
    private ReservationTime reservationTime;
    @NotNull
    private int numberOfMeals;
    @NotNull
    @ManyToOne
    private Restaurant restaurant;

    public Meal() {
    }

    public Meal(String name, String description, double price, Restaurant restaurant, LocalDate date,
            int numberOfMeals, ReservationTime reservationTime) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.restaurant = restaurant;
        this.date = date;
        this.numberOfMeals = numberOfMeals;
        this.reservationTime = reservationTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Date cannot be in the past");
        }
        this.date = date;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public int getNumberOfMeals() {
        return numberOfMeals;
    }

    public void setNumberOfMeals(int numberOfMeals) {
        this.numberOfMeals = numberOfMeals;
    }

    public boolean isAvailable() {
        return numberOfMeals > 0;
    }

    public void reserveMeal() {
        if (isAvailable()) {
            numberOfMeals--;
        } else {
            throw new IllegalStateException("No meals available for reservation");
        }
    }

    public void cancelMeal() {
        numberOfMeals++;
    }

    public ReservationTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(ReservationTime reservationTime) {
        this.reservationTime = reservationTime;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", date=" + date +
                ", restaurant=" + restaurant +
                '}';
    }

}
