package tqs.reservation.demo.service;

import java.time.LocalTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tqs.reservation.demo.model.Reservation;
import tqs.reservation.demo.model.ReservationStatus;
import tqs.reservation.demo.model.ReservationTime;
import tqs.reservation.demo.model.Restaurant;
import tqs.reservation.demo.repository.RestaurantRepository;

@Service
public class RestaurantService {

    @Autowired
    private RestaurantRepository restaurantRepository;

    public List<Restaurant> getAllRestaurants() {
        return restaurantRepository.findAll();
    }

    public Restaurant getRestaurantById(Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        return restaurantRepository.findById(id).orElse(null);
    }

    public Restaurant createRestaurant(Restaurant restaurant) {
        return restaurantRepository.save(restaurant);
    }

}
