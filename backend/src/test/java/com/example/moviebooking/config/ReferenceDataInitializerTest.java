package com.example.moviebooking.config;

import com.example.moviebooking.entity.City;
import com.example.moviebooking.repository.CityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ReferenceDataInitializerTest {

    @Autowired
    private ReferenceDataInitializer referenceDataInitializer;

    @Autowired
    private CityRepository cityRepository;

    @BeforeEach
    void setUp() {
        cityRepository.deleteAll();
    }

    @Test
    void seedIndianCitiesInsertsDefaultsWhenNoIndianCitiesExist() {
        referenceDataInitializer.seedIndianCities();

        assertThat(cityRepository.countByCountryIgnoreCase("India")).isEqualTo(6);
        assertThat(cityRepository.findTop6ByCountryIgnoreCaseOrderByDisplayOrderAsc("India"))
                .extracting(City::getName)
                .containsExactly("Mumbai", "Delhi", "Bengaluru", "Hyderabad", "Chennai", "Pune");
    }

    @Test
    void seedIndianCitiesDoesNothingWhenIndianCitiesAlreadyExist() {
        City existing = new City();
        existing.setName("Jaipur");
        existing.setCountry("India");
        existing.setDisplayOrder(1);
        cityRepository.save(existing);

        referenceDataInitializer.seedIndianCities();

        assertThat(cityRepository.countByCountryIgnoreCase("India")).isEqualTo(1);
        assertThat(cityRepository.findTop6ByCountryIgnoreCaseOrderByDisplayOrderAsc("India"))
                .extracting(City::getName)
                .containsExactly("Jaipur");
    }
}
