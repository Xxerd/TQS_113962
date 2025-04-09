package tqs.reservation.demo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import tqs.reservation.demo.model.Restaurant;
import tqs.reservation.demo.repository.RestaurantRepository;
import tqs.reservation.demo.service.RestaurantService;


class RestaurantServiceTest {

    @Mock
    private RestaurantRepository restaurantRepository;

    @InjectMocks
    private RestaurantService restaurantService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetRestaurantById() {
        Restaurant mockRestaurant = new Restaurant("Test Restaurant", 40.0, -8.0, 50);
        mockRestaurant.setId(1L);
        when(restaurantRepository.findById(1L)).thenReturn(Optional.of(mockRestaurant));

        Restaurant restaurant = restaurantService.getRestaurantById(1L);

        assertNotNull(restaurant);
        assertEquals("Test Restaurant", restaurant.getName());
        verify(restaurantRepository, times(1)).findById(1L);
    }

    @Test
    void testGetRestaurantById_NotFound() {
        when(restaurantRepository.findById(99L)).thenReturn(Optional.empty());

        Restaurant restaurant = restaurantService.getRestaurantById(99L);

        assertNull(restaurant);
        verify(restaurantRepository, times(1)).findById(99L);
    }

    @Test
    void testGetRestaurantById_NullId() {
        Restaurant restaurant = restaurantService.getRestaurantById(null);

        assertNull(restaurant);
        verify(restaurantRepository, never()).findById(any());
    }

    @Test
    void testGetRestaurantById_InvalidId() {
        Restaurant restaurant = restaurantService.getRestaurantById(-1L);

        assertNull(restaurant);
        verify(restaurantRepository, never()).findById(any());
    }

}
