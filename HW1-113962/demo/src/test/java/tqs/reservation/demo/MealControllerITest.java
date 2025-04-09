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
import tqs.reservation.demo.model.ReservationTime;
import tqs.reservation.demo.model.Restaurant;
import tqs.reservation.demo.repository.MealRepository;
import tqs.reservation.demo.repository.RestaurantRepository;

@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
class MealControllerITest {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:latest");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private MealRepository mealRepository;

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeEach
    public void setUp() {
        mealRepository.deleteAll();
        restaurantRepository.deleteAll();

        Restaurant restaurant = new Restaurant("Test Restaurant", 40.0, -8.0, 50);
        Restaurant restaurant2 = new Restaurant("Test Restaurant 2", 40.0, -8.0, 50);
        
        restaurantRepository.save(restaurant);
        restaurantRepository.save(restaurant2);

        mealRepository.save(new Meal("Meal 1", "Description 1", 10.0, restaurant,
                java.time.LocalDate.of(2025, 4, 10), 10, ReservationTime.LUNCH));
        mealRepository.save(new Meal("Meal 2", "Description 2", 15.0, restaurant,
                java.time.LocalDate.of(2025, 4, 11), 5, ReservationTime.DINNER));
    }

    @AfterEach
    public void resetDb() {
        mealRepository.deleteAll();
        restaurantRepository.deleteAll();
    }

    @Test
    void testGetAllMeals() throws Exception {
        mockMvc.perform(get("/apiV1/meals"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Meal 1")))
                .andExpect(jsonPath("$[1].name", is("Meal 2")));
    }

    @Test
    void testGetMealsByRestaurantId() throws Exception {

        Long restaurantId = restaurantRepository.findAll().get(0).getId();

        mockMvc.perform(get("/apiV1/meals/restaurant")
                .param("restaurantId", String.valueOf(restaurantId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Meal 1")))
                .andExpect(jsonPath("$[1].name", is("Meal 2")));
    }

    @Test
    void testCreateMeal() throws Exception {
        Long restaurantId = restaurantRepository.findAll().get(0).getId();
        String mealJson = """
                    {
                        "name": "New Meal",
                        "description": "New Meal Description",
                        "price": 20.0,
                        "restaurant": { "id": %d },
                        "date": "2025-04-12",
                        "numberOfMeals": 8,
                        "reservationTime": "LUNCH"
                    }
                """.formatted(restaurantId);

        mockMvc.perform(post("/apiV1/meals/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mealJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is("New Meal")))
                .andExpect(jsonPath("$.description", is("New Meal Description")))
                .andExpect(jsonPath("$.price", is(20.0)));
    }

    @Test
    void testGetMealsByRestaurantIdAndDate() throws Exception {

        Long restaurantId = restaurantRepository.findAll().get(0).getId();

        mockMvc.perform(get("/apiV1/meals/restaurant/date")
                .param("restaurantId", String.valueOf(restaurantId))
                .param("date", "2025-04-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("Meal 1")));
    }

    @Test
    void testGetMealsLeftById() throws Exception {
        Long mealId = mealRepository.findAll().get(0).getId();

        mockMvc.perform(get("/apiV1/meals/meals/left")
                .param("mealId", String.valueOf(mealId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", is(10))); // Number of meals left
    }

    @Test
    void testCreateMealWithInvalidFields() throws Exception {
        Long restaurantId = restaurantRepository.findAll().get(1).getId();

        String invalidMealJson = """
                    {
                        "name": "Invalid Meal",
                        "description": "Invalid Meal Description",
                        "price": -10.0,
                        "restaurant": { "id": %d },
                        "date": "2025-04-12",
                        "numberOfMeals": 8,
                        "reservationTime": "LUNCH"
                    }
                """.formatted(restaurantId);

        mockMvc.perform(post("/apiV1/meals/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidMealJson))
                .andExpect(status().isBadRequest());

        String missingRestaurantJson = """
                    {
                        "name": "Invalid Meal",
                        "description": "Invalid Meal Description",
                        "price": 10.0,
                        "date": "2025-04-12",
                        "numberOfMeals": 8,
                        "reservationTime": "LUNCH"
                    }
                """;

        mockMvc.perform(post("/apiV1/meals/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(missingRestaurantJson))
                .andExpect(status().isBadRequest());

        String pastDateMealJson = """
                    {
                    "name": "Past Meal",
                    "description": "Past Meal Description",
                    "price": 10.0,
                    "restaurant": { "id": %d },
                    "date": "2020-01-01",
                    "numberOfMeals": 5,
                    "reservationTime": "DINNER"
                    }
                """.formatted(restaurantId);

        mockMvc.perform(post("/apiV1/meals/create")
                .contentType(MediaType.APPLICATION_JSON)
                .content(pastDateMealJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetMealsForRestaurantWithNoMeals() throws Exception {
        Long restaurantId = restaurantRepository.findAll().get(1).getId();

        mockMvc.perform(get("/apiV1/meals/restaurant")
                .param("restaurantId", String.valueOf(restaurantId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0))); // No meals available
    }

}