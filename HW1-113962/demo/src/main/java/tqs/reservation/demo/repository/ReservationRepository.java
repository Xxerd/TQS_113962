package tqs.reservation.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import tqs.reservation.demo.model.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    Reservation findByReservationCode(String reservationCode);

    List<Reservation> findBycustomerEmail(String email);

    @Query("SELECT r FROM Reservation r WHERE r.meal.restaurant.id = :restaurantId AND r.meal.date = :day")
    List<Reservation> findByRestaurantAndDay(@Param("restaurantId") Long restaurantId, @Param("day") LocalDate day);

}
