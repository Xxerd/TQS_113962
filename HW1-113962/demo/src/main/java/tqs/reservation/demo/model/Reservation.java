package tqs.reservation.demo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.Email;
import java.security.SecureRandom;

@Entity
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String reservationCode = new SecureRandom().nextInt() + "";
    private String customerName;
    @Email
    private String customerEmail;
    private ReservationStatus status = ReservationStatus.PENDING;

    private ReservationTime reservationTime;

    @ManyToOne
    private Meal meal;

    public Reservation() {
    }

    public Reservation(Long id, String reservationCode, String customerName, String customerEmail,
            ReservationStatus status, Meal meal, ReservationTime reservationTime) {
        this.id = id;
        this.reservationCode = reservationCode;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.status = status;
        this.meal = meal;
        this.reservationTime = reservationTime;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getReservationCode() {
        return reservationCode;
    }

    public void setReservationCode(String reservationCode) {
        this.reservationCode = reservationCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    public void setStatus(ReservationStatus status) {
        this.status = status;
    }

    public Meal getMeal() {
        return meal;
    }

    public void setMeal(Meal meal) {
        this.meal = meal;
    }

    public ReservationTime getReservationTime() {
        return reservationTime;
    }

    public void setReservationTime(ReservationTime reservationTime) {
        this.reservationTime = reservationTime;
    }

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", reservationCode='" + reservationCode + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerEmail='" + customerEmail + '\'' +
                ", status=" + status +
                ", meal=" + meal +
                '}';
    }

}
