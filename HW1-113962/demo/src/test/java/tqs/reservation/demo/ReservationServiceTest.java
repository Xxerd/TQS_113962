package tqs.reservation.demo;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import tqs.reservation.demo.model.Reservation;
import tqs.reservation.demo.model.ReservationStatus;
import tqs.reservation.demo.model.ReservationTime;
import tqs.reservation.demo.repository.ReservationRepository;
import tqs.reservation.demo.service.ReservationService;

class ReservationServiceTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllReservations() {
        List<Reservation> mockReservations = List.of(new Reservation(), new Reservation());
        when(reservationRepository.findAll()).thenReturn(mockReservations);

        List<Reservation> reservations = reservationService.getAllReservations();

        assertEquals(2, reservations.size());
        verify(reservationRepository, times(1)).findAll();
    }

    @Test
    void testGetReservationById() {
        Reservation mockReservation = new Reservation();
        mockReservation.setId(1L);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(mockReservation));

        Reservation reservation = reservationService.getReservationById(1L);

        assertNotNull(reservation);
        assertEquals(1L, reservation.getId());
        verify(reservationRepository, times(1)).findById(1L);
    }

    @Test
    void testCancelReservation() {
        Reservation mockReservation = new Reservation();
        mockReservation.setId(1L);
        mockReservation.setStatus(ReservationStatus.CONFIRMED);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(mockReservation));

        Reservation cancelledReservation = reservationService.cancelReservation(1L);

        assertEquals(ReservationStatus.CANCELLED, cancelledReservation.getStatus());
        verify(reservationRepository, times(1)).save(mockReservation);
    }

    @Test
    void testGetNumberOfPersonsOnRestaurant() {
        Reservation mockReservation = new Reservation();
        mockReservation.setStatus(ReservationStatus.CONFIRMED);
        mockReservation.setReservationTime(ReservationTime.LUNCH);

        Reservation mockReservation2 = new Reservation();
        mockReservation2.setStatus(ReservationStatus.CONFIRMED);
        mockReservation2.setReservationTime(ReservationTime.DINNER);

        when(reservationRepository.findByRestaurantAndDay(1L, LocalDate.of(2025, 4, 10)))
                .thenReturn(List.of(mockReservation, mockReservation2));

        when(reservationRepository.findByRestaurantAndDay(1L, LocalDate.of(2025, 4, 10)))
                .thenReturn(List.of(mockReservation));

        int count = reservationService.getNumberOfPersonsOnRestaurant(1L, LocalDate.of(2025, 4, 10),
                ReservationTime.LUNCH);

        assertEquals(1, count);
        verify(reservationRepository, times(1)).findByRestaurantAndDay(1L, LocalDate.of(2025, 4, 10));
    }

    @Test
    void testCancelReservationAlreadyCancelled() {
        Reservation mockReservation = new Reservation();
        mockReservation.setId(1L);
        mockReservation.setStatus(ReservationStatus.CANCELLED);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(mockReservation));

        Reservation cancelledReservation = reservationService.cancelReservation(1L);

        assertEquals(ReservationStatus.CANCELLED, cancelledReservation.getStatus());
        verify(reservationRepository, never()).save(mockReservation);
    }

    @Test
    void testCancelReservationAlreadyCompleted() {
        Reservation mockReservation = new Reservation();
        mockReservation.setId(1L);
        mockReservation.setStatus(ReservationStatus.COMPLETED);
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(mockReservation));

        Reservation cancelledReservation = reservationService.cancelReservation(1L);

        assertEquals(ReservationStatus.COMPLETED, cancelledReservation.getStatus());
        verify(reservationRepository, never()).save(mockReservation);
    }

    @Test
    void testGetReservationsForRestaurantWithNoReservations() {
        when(reservationRepository.findByRestaurantAndDay(1L, LocalDate.of(2025, 4, 10)))
                .thenReturn(List.of());

        List<Reservation> reservations = reservationService.getReservationByRestaurantAndDay(1L,
                LocalDate.of(2025, 4, 10));

        assertTrue(reservations.isEmpty());
        verify(reservationRepository, times(1)).findByRestaurantAndDay(1L, LocalDate.of(2025, 4, 10));
    }

    @Test
    void testGetReservationWithNullId() {
        Reservation reservation = reservationService.getReservationById(null);

        assertNull(reservation);
        verify(reservationRepository, never()).findById(any());
    }

    @Test
    void testGetReservationWithInvalidId() {
        when(reservationRepository.findById(999L)).thenReturn(Optional.empty());

        Reservation reservation = reservationService.getReservationById(999L);

        assertNull(reservation);
        verify(reservationRepository, times(1)).findById(999L);
    }

}