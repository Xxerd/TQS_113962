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
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import tqs.reservation.demo.model.Meal;
import tqs.reservation.demo.model.Reservation;
import tqs.reservation.demo.model.ReservationStatus;
import tqs.reservation.demo.model.ReservationTime;
import tqs.reservation.demo.model.Restaurant;
import tqs.reservation.demo.repository.MealRepository;
import tqs.reservation.demo.repository.ReservationRepository;
import tqs.reservation.demo.repository.RestaurantRepository;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class ReservationControllerITest {

        @Container
        static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private RestaurantRepository restaurantRepository;

        @Autowired
        private MealRepository mealRepository;

        @Autowired
        private ReservationRepository reservationRepository;

        @DynamicPropertySource
        static void configureProperties(DynamicPropertyRegistry registry) {
                registry.add("spring.datasource.url", postgres::getJdbcUrl);
                registry.add("spring.datasource.username", postgres::getUsername);
                registry.add("spring.datasource.password", postgres::getPassword);
        }

        @BeforeEach
        public void setUp() {
                reservationRepository.deleteAll();
                mealRepository.deleteAll();
                restaurantRepository.deleteAll();

                Restaurant restaurant = new Restaurant("Test Restaurant", 40.0, -8.0, 2); // Max capacity = 2
                restaurantRepository.save(restaurant);

                Meal meal = new Meal("Test Meal", "Delicious test meal", 10.0, restaurant,
                                java.time.LocalDate.of(2025, 4, 10), 10, ReservationTime.LUNCH);
                mealRepository.save(meal);
        }

        @AfterEach
        public void resetDb() {
                reservationRepository.deleteAll();
                mealRepository.deleteAll();
                restaurantRepository.deleteAll();
        }

        @Test
        void testCancelConfirmedReservation() throws Exception {
                // Arrange: Add a confirmed reservation
                Meal meal = mealRepository.findAll().get(0);
                Reservation reservation = new Reservation(null, "CODE1", "John Doe", "john@example.com",
                                ReservationStatus.CONFIRMED, meal, ReservationTime.LUNCH);
                reservationRepository.save(reservation);

                // Act & Assert: Attempt to cancel a confirmed reservation
                mockMvc.perform(put("/apiV1/reservations/update/" + reservation.getReservationCode())
                                .param("status", "CANCELLED"))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().string(containsString(
                                                "Reservation cannot be cancelled either because it is already cancelled or completed")));
        }

        @Test
        void testCompletePendingReservation() throws Exception {
                Meal meal = mealRepository.findAll().get(0);
                Reservation reservation = new Reservation(null, "CODE2", "Jane Doe", "jane@example.com",
                                ReservationStatus.PENDING, meal, ReservationTime.LUNCH);
                reservationRepository.save(reservation);

                mockMvc.perform(put("/apiV1/reservations/update/" + reservation.getReservationCode())
                                .param("status", "COMPLETED"))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().string(containsString(
                                                "Reservation cannot be completed either because it is already cancelled or completed")));
        }

        @Test
        void testConfirmReservationWhenRestaurantIsFull() throws Exception {
                Meal meal = mealRepository.findAll().get(0);
                Reservation reservation1 = new Reservation(null, "CODE3", "Alice", "alice@example.com",
                                ReservationStatus.CONFIRMED, meal, ReservationTime.LUNCH);
                Reservation reservation2 = new Reservation(null, "CODE4", "Bob", "bob@example.com",
                                ReservationStatus.CONFIRMED, meal, ReservationTime.LUNCH);
                reservationRepository.save(reservation1);
                reservationRepository.save(reservation2);

                Reservation reservation3 = new Reservation(null, "CODE5", "Charlie", "charlie@example.com",
                                ReservationStatus.PENDING, meal, ReservationTime.LUNCH);
                reservationRepository.save(reservation3);

                mockMvc.perform(put("/apiV1/reservations/update/" + reservation3.getReservationCode())
                                .param("status", "CONFIRMED"))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().string(containsString("No available capacity")));
        }

        @Test
        void testConfirmPendingReservation() throws Exception {
                Meal meal = mealRepository.findAll().get(0);
                Reservation reservation = new Reservation(null, "CODE6", "David", "david@example.com",
                                ReservationStatus.PENDING, meal, ReservationTime.LUNCH);
                reservationRepository.save(reservation);

                mockMvc.perform(put("/apiV1/reservations/update/" + reservation.getReservationCode())
                                .param("status", "CONFIRMED"))
                                .andExpect(status().isOk())
                                .andExpect(content().string(containsString("Reservation status updated to confirmed")));
        }

        @Test
        void testCreateReservationWhenNoMealsAvailable() throws Exception {
                Meal meal = mealRepository.findAll().get(0);
                meal.setNumberOfMeals(0);
                mealRepository.save(meal);

                String reservationJson = """
                                    {
                                        "customerName": "Eve",
                                        "customerEmail": "eve@example.com",
                                        "reservationTime": "LUNCH",
                                        "meal": { "id": %d }
                                    }
                                """.formatted(meal.getId());

                mockMvc.perform(post("/apiV1/reservations/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(reservationJson))
                                .andExpect(status().isBadRequest())
                                .andExpect(content().string(containsString("Meal not available")));
        }

        @Test
        void testCreateReservationWithMissingMealId() throws Exception {
                String reservationJson = """
                                    {
                                        "customerName": "Missing Meal User",
                                        "customerEmail": "missingmeal@example.com",
                                        "reservationTime": "LUNCH"
                                    }
                                """;

                mockMvc.perform(post("/apiV1/reservations/create")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(reservationJson))
                                .andExpect(status().isBadRequest());
        }

        @Test
        void testUpdateReservationToInvalidStatus() throws Exception {
                String invalidStatus = "INVALID_STATUS";

                mockMvc.perform(put("/apiV1/reservations/update/CODE123")
                                .param("status", invalidStatus))
                                .andExpect(status().isBadRequest());
        }

}