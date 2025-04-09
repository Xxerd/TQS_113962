package tqs.reservation.demo.controller;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import tqs.reservation.demo.model.Meal;
import tqs.reservation.demo.model.Reservation;
import tqs.reservation.demo.model.ReservationStatus;
import tqs.reservation.demo.service.MealService;
import tqs.reservation.demo.service.ReservationService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/apiV1/reservations")
public class ReservationController {

    @Autowired
    private ReservationService reservationService;
    Logger log = LoggerFactory.getLogger(ReservationController.class);

    @Autowired
    private MealService mealService;

    @GetMapping
    @Operation(summary = "Get all reservations")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        if (reservations.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{reservationCode}")
    @Operation(summary = "Get reservation by reservation code")
    public ResponseEntity<Reservation> getReservationByCode(@PathVariable String reservationCode) {
        Reservation reservation = reservationService.getReservationByReservationCode(reservationCode);
        if (reservation == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(reservation);
    }

    @GetMapping("/user/{email}")
    @Operation(summary = "Get reservations by user email")
    public ResponseEntity<List<Reservation>> getReservationsByUserEmail(@PathVariable String email) {
        List<Reservation> reservations = reservationService.getReservationsByUserEmail(email);
        if (reservations.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(reservations);
    }

    @PutMapping("/update/{reservationCode}")
    @Operation(summary = "Update reservation status")
    public ResponseEntity<String> updateReservation(@PathVariable String reservationCode,
            @RequestParam ReservationStatus status) {
        Reservation reservation = reservationService.getReservationByReservationCode(reservationCode);
        if (reservation == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Reservation not found");
        }

        switch (status) {
            case CANCELLED:
                if (reservation.getStatus() == ReservationStatus.PENDING) {
                    synchronized (reservation.getMeal()) {
                        reservation.getMeal().cancelMeal();
                    }
                    reservationService.cancelReservation(reservation.getId());
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                            "Reservation cannot be cancelled either because it is already cancelled or completed");
                }
                break;
            case COMPLETED:
                if (reservation.getStatus() == ReservationStatus.CONFIRMED) {
                    reservationService.completeReservation(reservation.getId());
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                            "Reservation cannot be completed either because it is already cancelled or completed");
                }
                break;
            case CONFIRMED:
                if (reservation.getStatus() == ReservationStatus.PENDING) {
                    int capacity = reservationService.getNumberOfPersonsOnRestaurant(
                            reservation.getMeal().getRestaurant().getId(),
                            reservation.getMeal().getDate(), reservation.getReservationTime());
                    if (capacity >= reservation.getMeal().getRestaurant().getMaxCapacity()) {
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No available capacity");
                    }
                    reservation.setStatus(ReservationStatus.CONFIRMED);
                    reservationService.createReservation(reservation);
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                            "Reservation cannot be confirmed either because it is already cancelled or completed");
                }
                break;

            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid reservation status");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Reservation status updated to " + status);
    }

    @PostMapping("/create")
    @Operation(summary = "Create a new reservation")
    public ResponseEntity<?> createReservation(@RequestBody Reservation reservation) {
        if (reservation.getMeal() == null || reservation.getMeal().getId() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Meal ID is required");
        }
        Meal meal = mealService.getMealById(reservation.getMeal().getId());
        
        if (!meal.isAvailable()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Meal not available");
        }
        meal.reserveMeal();
        Reservation createdReservation = reservationService.createReservation(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
    }

    @GetMapping("/restaurant/date")
    public ResponseEntity<List<Reservation>> getMethodName(@RequestParam Long restaurantId,
            @RequestParam LocalDate date) {

        List<Reservation> reservations = reservationService.getReservationByRestaurantAndDay(restaurantId, date);
        return ResponseEntity.status(HttpStatus.OK).body(reservations);

    }

}
