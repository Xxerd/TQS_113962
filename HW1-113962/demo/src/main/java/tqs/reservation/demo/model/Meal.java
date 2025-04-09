package tqs.reservation.demo.model;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private double price;
    private LocalDate date;
    private ReservationTime reservationTime;
    private int numberOfMeals;

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
