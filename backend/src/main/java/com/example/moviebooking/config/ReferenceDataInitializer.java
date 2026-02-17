package com.example.moviebooking.config;

import com.example.moviebooking.entity.City;
import com.example.moviebooking.repository.CityRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ReferenceDataInitializer implements ApplicationRunner {

    private static final String INDIA = "India";

    private final CityRepository cityRepository;

    public ReferenceDataInitializer(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        seedIndianCities();
    }

    void seedIndianCities() {
        if (cityRepository.countByCountryIgnoreCase(INDIA) > 0) {
            return;
        }

        cityRepository.saveAll(List.of(
                createCity("Mumbai", 1),
                createCity("Delhi", 2),
                createCity("Bengaluru", 3),
                createCity("Hyderabad", 4),
                createCity("Chennai", 5),
                createCity("Pune", 6)
        ));
    }

    private City createCity(String name, int order) {
        City city = new City();
        city.setName(name);
        city.setCountry(INDIA);
        city.setDisplayOrder(order);
        return city;
    }
}
