package tqs.reservation.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tqs.reservation.demo.model.Meal;
import tqs.reservation.demo.repository.MealRepository;

@Service
public class MealService {

    @Autowired
    private MealRepository mealRepository;

    public List<Meal> getAllMeals() {
        return mealRepository.findAll();
    }

    public Meal getMealById(Long id) {
        return mealRepository.findById(id).orElse(null);
    }

    public Meal createMeal(Meal meal) {
        return mealRepository.save(meal);
    }

    public Meal updateMeal(Long id, Meal meal) {
        Meal existingMeal = mealRepository.findById(id).orElse(null);
        if (existingMeal != null) {
            existingMeal.setName(meal.getName());
            existingMeal.setDescription(meal.getDescription());
            existingMeal.setPrice(meal.getPrice());
            return mealRepository.save(existingMeal);
        }
        return null;
    }

    public void deleteMeal(Long id) {
        mealRepository.deleteById(id);
    }

    public List<Meal> getMealsByRestaurantId(Long restaurantId) {
        return mealRepository.findByRestaurantId(restaurantId);
    }

    public List<Meal> getMealsByDate(LocalDate date) {
        return mealRepository.findByDate(date);
    }

    public List<Meal> getMealsByRestaurantIdAndDate(Long restaurantId, LocalDate date) {
        return mealRepository.findByRestaurantIdAndDate(restaurantId, date);
    }

}
