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

import tqs.reservation.demo.model.Reservation;
import tqs.reservation.demo.model.Restaurant;
import tqs.reservation.demo.repository.MealRepository;
import tqs.reservation.demo.repository.ReservationRepository;
import tqs.reservation.demo.repository.RestaurantRepository;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class RestaurantControllerITest {

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

    @AfterEach
    public void resetDb() {
        reservationRepository.deleteAll();
        mealRepository.deleteAll();
        restaurantRepository.deleteAll();
    }

    @Test
    void testGetAllRestaurants() throws Exception {
        restaurantRepository.save(new Restaurant("Cantina Universitária de Santiago", 40.630581, -8.656, 20));
        restaurantRepository.save(new Restaurant("Complexo de Refeitórios do Castro", 40.6215, -8.643022, 50));

        mockMvc.perform(get("/apiV1/restaurants"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Cantina Universitária de Santiago")))
                .andExpect(jsonPath("$[1].name", is("Complexo de Refeitórios do Castro")));
    }

    @Test
    void testGetRestaurantById() throws Exception {
        Restaurant restaurant = restaurantRepository
                .save(new Restaurant("Cantina Universitária de Santiago", 40.630581, -8.656, 20));

        mockMvc.perform(get("/apiV1/restaurants/" + restaurant.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("Cantina Universitária de Santiago")))
                .andExpect(jsonPath("$.latitude", is(40.630581)))
                .andExpect(jsonPath("$.longitude", is(-8.656)))
                .andExpect(jsonPath("$.maxCapacity", is(20)));
    }

    @Test
    void testGetRestaurantById_NotFound() throws Exception {
        mockMvc.perform(get("/apiV1/restaurants/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreateRestaurant() throws Exception {
        String restaurantJson = """
                    {
                        "name": "New Restaurant",
                        "latitude": 40.123456,
                        "longitude": -8.654321,
                        "maxCapacity": 30
                    }
                """;

        mockMvc.perform(post("/apiV1/restaurants/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(restaurantJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("New Restaurant")))
                .andExpect(jsonPath("$.latitude", is(40.123456)))
                .andExpect(jsonPath("$.longitude", is(-8.654321)))
                .andExpect(jsonPath("$.maxCapacity", is(30)));
    }

    @Test
    void testGetRestaurantCapacity() throws Exception {
        Restaurant restaurant = restaurantRepository
                .save(new Restaurant("Cantina Universitária de Santiago", 40.630581, -8.656, 20));

        mockMvc.perform(get("/apiV1/restaurants/capacity")
                .param("restaurantId", String.valueOf(restaurant.getId()))
                .param("date", "2025-04-10")
                .param("time", "LUNCH"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0]", is(0))) // Current persons
                .andExpect(jsonPath("$[1]", is(20))); // Max capacity
    }

    @Test
    void testCreateRestaurantWithoutName() throws Exception {
        String restaurantJson = """
                    {
                        "latitude": 40.123456,
                        "longitude": -8.654321,
                        "maxCapacity": 30
                    }
                """;

        mockMvc.perform(post("/apiV1/restaurants/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(restaurantJson))
                .andExpect(status().isBadRequest());
    }
}