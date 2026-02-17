package com.example.moviebooking.service;

import com.example.moviebooking.entity.City;
import com.example.moviebooking.repository.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ReferenceDataServiceTest {

    @Autowired
    private ReferenceDataService referenceDataService;

    @Autowired
    private CityRepository cityRepository;

    @BeforeEach
    void setUp() {
        cityRepository.deleteAll();
        cityRepository.saveAll(List.of(
                createCity("Mumbai", "India", 1),
                createCity("Delhi", "India", 2),
                createCity("Bengaluru", "India", 3),
                createCity("Hyderabad", "India", 4),
                createCity("Chennai", "India", 5),
                createCity("Pune", "India", 6),
                createCity("Kolkata", "India", 7),
                createCity("Paris", "France", 1)
        ));
    }

    @Test
    void getTopIndianCitiesReturnsFirstSixIndianCities() {
        List<String> cities = referenceDataService.getTopIndianCities();

        assertThat(cities).hasSize(6);
        assertThat(cities).containsExactly(
                "Mumbai",
                "Delhi",
                "Bengaluru",
                "Hyderabad",
                "Chennai",
                "Pune"
        );
    }

    private City createCity(String name, String country, int order) {
        City city = new City();
        city.setName(name);
        city.setCountry(country);
        city.setDisplayOrder(order);
        return city;
    }
}
