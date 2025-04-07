package tqs.reservation.demo.model;

public enum ReservationStatus {
    CONFIRMED,
    CANCELLED,
    COMPLETED;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

}
