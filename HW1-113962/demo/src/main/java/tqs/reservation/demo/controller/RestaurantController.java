package tqs.reservation.demo.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import tqs.reservation.demo.model.ReservationTime;
import tqs.reservation.demo.model.Restaurant;
import tqs.reservation.demo.service.ReservationService;
import tqs.reservation.demo.service.RestaurantService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/apiV1/restaurants")
public class RestaurantController {
    @Autowired
    private RestaurantService restaurantService;
    @Autowired
    private ReservationService reservationService;

    @GetMapping
    @Operation(summary = "Get all restaurants")
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        if (restaurants.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get restaurant by ID")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable Long id) {
        Restaurant restaurant = restaurantService.getRestaurantById(id);
        if (restaurant == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(restaurant);
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new restaurant")
    public ResponseEntity<Restaurant> createRestaurant(@Valid @RequestBody Restaurant restaurant) {
        Restaurant createdRestaurant = restaurantService.createRestaurant(restaurant);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRestaurant);
    }

    @GetMapping("/capacity")
    @Operation(summary = "Get number of persons on restaurant, and max capacity")
    public ResponseEntity<List<Integer>> getRestaurantCapacity(@RequestParam Long restaurantId,
            @RequestParam LocalDate date, @RequestParam ReservationTime time) {
        int capacity = reservationService.getNumberOfPersonsOnRestaurant(restaurantId, date, time);
        int maxCapacity = restaurantService.getRestaurantById(restaurantId).getMaxCapacity();
        return ResponseEntity.ok(List.of(capacity, maxCapacity));

    }

}
