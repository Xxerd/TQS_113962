package tqs.reservation.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import tqs.reservation.demo.model.Meal;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findByRestaurantId(Long restaurantId);

    List<Meal> findByRestaurantIdAndDate(Long restaurantId, LocalDate date);

    List<Meal> findByDate(LocalDate date);

}
