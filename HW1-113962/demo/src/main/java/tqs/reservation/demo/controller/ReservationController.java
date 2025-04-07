package tqs.reservation.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import tqs.reservation.demo.model.Reservation;
import tqs.reservation.demo.model.ReservationStatus;
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

    @GetMapping
    public ResponseEntity<List<Reservation>> getAllReservations() {
        List<Reservation> reservations = reservationService.getAllReservations();
        if (reservations.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(reservations);
    }

    @GetMapping("/{reservationCode}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable String reservationCode) {
        Reservation reservation = reservationService.getReservationByReservationCode(reservationCode);
        if (reservation == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(reservation);
    }

    @PutMapping("/update/{reservationCode}")
    public ResponseEntity<String> updateReservation(@PathVariable String reservationCode,
            @RequestBody ReservationStatus status) {
        Reservation reservation = reservationService.getReservationByReservationCode(reservationCode);
        if (reservation == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        switch (status) {
            case CANCELLED:
                if (reservation.getStatus() == ReservationStatus.CONFIRMED) {
                    reservation = reservationService.cancelReservation(reservation.getId());
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                            "Reservation cannot be cancelled either because it is already cancelled or completed");
                }
                break;
            case COMPLETED:
                if (reservation.getStatus() == ReservationStatus.CONFIRMED) {
                    reservation = reservationService.completeReservation(reservation.getId());
                } else {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                            "Reservation cannot be completed either because it is already cancelled or completed");
                }
                break;

            default:
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid reservation status");
        }
        return ResponseEntity.status(HttpStatus.OK).body("Reservation status updated to " + status);
    }

    @PostMapping("/create")
    public ResponseEntity<Reservation> createReservation(@RequestBody Reservation reservation) {
        Reservation createdReservation = reservationService.createReservation(reservation);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdReservation);
    }

}
