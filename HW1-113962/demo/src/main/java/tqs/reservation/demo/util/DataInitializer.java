package tqs.reservation.demo.util;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import tqs.reservation.demo.repository.*;
import tqs.reservation.demo.model.*;

@Component
public class DataInitializer implements CommandLineRunner {

        @Autowired
        private RestaurantRepository restaurantRepository;
        @Autowired
        private MealRepository mealRepository;

        @Override
        public void run(String... args) throws Exception {

                if (restaurantRepository.count() > 0) {
                        return; // Data already initialized

                }
                Restaurant Cantina = new Restaurant("Cantina Universitária de Santiago", 40.630581, -8.656, 20);
                Restaurant Refeitorio = new Restaurant("Complexo de Refeitórios do Castro", 40.6215, -8.643022, 2);
                restaurantRepository.save(Cantina);
                restaurantRepository.save(Refeitorio);

                mealRepository.save(
                                new Meal("Bacalhau à Brás",
                                                "Prato tradicional português com bacalhau desfiado, batata palha e ovos",
                                                12.5, Cantina, LocalDate.of(2025, 4, 10), 2, ReservationTime.LUNCH));
                mealRepository.save(new Meal("Arroz de Polvo", "Arroz cremoso com polvo tenro e coentros frescos", 14.9,
                                Cantina, LocalDate.of(2025, 4, 11), 2, ReservationTime.LUNCH));
                mealRepository.save(new Meal("Bitoque de Novilho",
                                "Bife de novilho com ovo estrelado, arroz e batata frita",
                                13.75, Refeitorio, LocalDate.of(2025, 4, 10), 2, ReservationTime.LUNCH));
                mealRepository
                                .save(new Meal("Caldo Verde",
                                                "Sopa tradicional portuguesa com couve galega e rodelas de chouriço",
                                                6.5,
                                                Refeitorio, LocalDate.of(2025, 4, 11), 2, ReservationTime.LUNCH));

        }
}
