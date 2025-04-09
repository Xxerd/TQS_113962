package tqs.reservation.demo.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import tqs.reservation.demo.model.ReservationStatus;

@Component
public class StringToReservationStatusConverter implements Converter<String, ReservationStatus> {

    @Override
    public ReservationStatus convert(String source) {
        try {
            return ReservationStatus.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid ReservationStatus value: " + source);
        }
    }
}
