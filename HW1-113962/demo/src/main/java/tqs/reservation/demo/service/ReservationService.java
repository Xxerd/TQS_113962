package tqs.reservation.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import tqs.reservation.demo.model.Reservation;
import tqs.reservation.demo.model.ReservationStatus;
import tqs.reservation.demo.model.ReservationTime;
import tqs.reservation.demo.repository.ReservationRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    public Reservation getReservationById(Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        return reservationRepository.findById(id).orElse(null);
    }

    public List<Reservation> getReservationsByUserEmail(String email) {
        return reservationRepository.findBycustomerEmail(email);
    }

    public Reservation getReservationByReservationCode(String reservationCode) {
        return reservationRepository.findByReservationCode(reservationCode);
    }

    public Reservation createReservation(Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    public Reservation cancelReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElse(null);
        if (reservation.getStatus() == ReservationStatus.CONFIRMED) {
            reservation.setStatus(ReservationStatus.CANCELLED);
            reservationRepository.save(reservation);
        }
        return reservation;
    }

    public Reservation completeReservation(Long id) {
        Reservation reservation = reservationRepository.findById(id).orElse(null);
        if (reservation.getStatus() == ReservationStatus.CONFIRMED) {
            reservation.setStatus(ReservationStatus.COMPLETED);
            reservationRepository.save(reservation);
        }
        return reservation;
    }

    public List<Reservation> getReservationByRestaurantAndDay(Long restaurantId, LocalDate day) {
        return reservationRepository.findByRestaurantAndDay(restaurantId, day);
    }

    public int getNumberOfPersonsOnRestaurant(Long restaurantId, LocalDate time, ReservationTime reservationTime) {
        List<Reservation> existingReservations = getReservationByRestaurantAndDay(restaurantId,
                time)
                .stream()
                .filter(r -> r.getReservationTime().equals(reservationTime)
                        && r.getStatus() == ReservationStatus.CONFIRMED)
                .toList();
        return existingReservations.size();

    }

}
