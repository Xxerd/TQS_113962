package tqs.reservation.demo;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import tqs.reservation.demo.model.Meal;
import tqs.reservation.demo.model.Reservation;
import tqs.reservation.demo.model.ReservationStatus;
import tqs.reservation.demo.model.ReservationTime;
import tqs.reservation.demo.model.Restaurant;
import tqs.reservation.demo.repository.ReservationRepository;
import tqs.reservation.demo.repository.MealRepository;
import tqs.reservation.demo.repository.RestaurantRepository;

@DataJpaTest
public class ReservationRepositoryTest {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MealRepository mealRepository;

    private Restaurant restaurant;
    private Meal meal;

    @BeforeEach
    void setUp() {
        // Create and save a restaurant
        restaurant = new Restaurant("Test Restaurant", 40.0, -8.0, 50);
        restaurantRepository.save(restaurant);

        // Create and save a meal
        meal = new Meal("Test Meal", "Delicious test meal", 10.0, restaurant, LocalDate.of(2025, 4, 10), 10,
                ReservationTime.LUNCH);
        mealRepository.save(meal);

        // Create and save reservations
        Reservation reservation1 = new Reservation(null, "CODE1", "John Doe", "john@example.com",
                ReservationStatus.CONFIRMED, meal, ReservationTime.LUNCH);
        Reservation reservation2 = new Reservation(null, "CODE2", "Jane Doe", "jane@example.com",
                ReservationStatus.PENDING, meal, ReservationTime.LUNCH);
        reservationRepository.save(reservation1);
        reservationRepository.save(reservation2);
    }

    @Test
    void testFindByRestaurantAndDay() {
        // Execute the query
        List<Reservation> reservations = reservationRepository.findByRestaurantAndDay(restaurant.getId(),
                LocalDate.of(2025, 4, 10));

        // Verify the results
        assertThat(reservations).hasSize(2); // Two reservations should be found
        assertThat(reservations.get(0).getMeal().getRestaurant().getId()).isEqualTo(restaurant.getId());
        assertThat(reservations.get(0).getMeal().getDate()).isEqualTo(LocalDate.of(2025, 4, 10));
    }

    @Test
    void testFindByRestaurantAndDay_NoReservationsForRestaurant() {
        // Create a new restaurant with no reservations
        Restaurant newRestaurant = new Restaurant("Empty Restaurant", 41.0, -9.0, 30);
        restaurantRepository.save(newRestaurant);

        // Execute the query
        List<Reservation> reservations = reservationRepository.findByRestaurantAndDay(newRestaurant.getId(),
                LocalDate.of(2025, 4, 10));

        // Verify the results
        assertThat(reservations).isEmpty(); // No reservations should be found
    }

    @Test
    void testFindByRestaurantAndDay_NoReservationsForDate() {
        // Execute the query for a date with no reservations
        List<Reservation> reservations = reservationRepository.findByRestaurantAndDay(restaurant.getId(),
                LocalDate.of(2025, 4, 11));

        // Verify the results
        assertThat(reservations).isEmpty(); // No reservations should be found
    }

    @Test
    void testFindByRestaurantAndDay_InvalidRestaurantId() {
        // Execute the query with an invalid restaurant ID
        List<Reservation> reservations = reservationRepository.findByRestaurantAndDay(-1L,
                LocalDate.of(2025, 4, 10));

        // Verify the results
        assertThat(reservations).isEmpty(); // No reservations should be found
    }

    @Test
    void testFindByRestaurantAndDay_InvalidDate() {
        // Execute the query with an invalid date (e.g., null)
        List<Reservation> reservations = reservationRepository.findByRestaurantAndDay(restaurant.getId(), null);

        // Verify the results
        assertThat(reservations).isEmpty(); // No reservations should be found
    }
}