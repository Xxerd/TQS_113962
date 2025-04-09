package tqs.reservation.demo.util;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import tqs.reservation.demo.model.ReservationTime;

@Component
public class StringToReservationTimeConverter implements Converter<String, ReservationTime> {

    @Override
    public ReservationTime convert(String source) {
        try {
            return ReservationTime.valueOf(source.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid ReservationStatus value: " + source);
        }
    }
}
