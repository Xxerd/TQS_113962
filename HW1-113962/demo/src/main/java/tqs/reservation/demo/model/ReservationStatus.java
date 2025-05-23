package tqs.reservation.demo.model;

public enum ReservationStatus {
    PENDING,
    CANCELLED,
    CONFIRMED,
    COMPLETED;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

}
