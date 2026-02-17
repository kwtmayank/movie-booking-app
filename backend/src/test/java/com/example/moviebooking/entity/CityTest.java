package com.example.moviebooking.entity;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CityTest {

    @Test
    void gettersAndSettersWork() {
        City city = new City();
        city.setName("Mumbai");
        city.setCountry("India");
        city.setDisplayOrder(1);

        assertThat(city.getId()).isNull();
        assertThat(city.getName()).isEqualTo("Mumbai");
        assertThat(city.getCountry()).isEqualTo("India");
        assertThat(city.getDisplayOrder()).isEqualTo(1);
    }
}
