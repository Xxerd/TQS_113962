package tqs.reservation.demo.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import tqs.reservation.demo.model.Meal;
import tqs.reservation.demo.service.MealService;

@RestController
@RequestMapping("/apiV1/meals")
public class MealController {

    @Autowired
    private MealService mealService;

    @GetMapping
    public ResponseEntity<List<Meal>> getAllMeals() {
        List<Meal> meals = mealService.getAllMeals();
        if (meals.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(meals);
    }

    @GetMapping("/restaurant")
    public ResponseEntity<List<Meal>> getMealsByRestaurantId(@RequestParam Long restaurantId) {
        List<Meal> meals = mealService.getMealsByRestaurantId(restaurantId);
        if (meals.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(meals);
    }

    @PostMapping("/create")
    public ResponseEntity<Meal> createMeal(@RequestBody Meal meal) {
        Meal createdMeal = mealService.createMeal(meal);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMeal);
    }

    @GetMapping("/restaurant/date")
    public ResponseEntity<List<Meal>> getMealsByRestaurantIdAndDate(@RequestParam Long restaurantId,
            @RequestParam LocalDate date) {
        List<Meal> meals = mealService.getMealsByRestaurantIdAndDate(restaurantId, date);
        if (meals.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(meals);
    }



}
