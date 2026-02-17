package com.example.moviebooking.repository;

import com.example.moviebooking.entity.City;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CityRepositoryTest {

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
                createCity("London", "UK", 1)
        ));
    }

    @Test
    void findTop6ByCountryReturnsOrderedCities() {
        List<City> cities = cityRepository.findTop6ByCountryIgnoreCaseOrderByDisplayOrderAsc("india");

        assertThat(cities).hasSize(6);
        assertThat(cities.get(0).getName()).isEqualTo("Mumbai");
        assertThat(cities.get(5).getName()).isEqualTo("Pune");
    }

    @Test
    void countByCountryIgnoreCaseCountsOnlyMatchingCountry() {
        assertThat(cityRepository.countByCountryIgnoreCase("india")).isEqualTo(7);
        assertThat(cityRepository.countByCountryIgnoreCase("uk")).isEqualTo(1);
    }

    private City createCity(String name, String country, int order) {
        City city = new City();
        city.setName(name);
        city.setCountry(country);
        city.setDisplayOrder(order);
        return city;
    }
}
