package tqs.reservation.demo.model;

public enum ReservationTime {
    LUNCH,
    DINNER;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

}
