package com.example.moviebooking.repository;

import com.example.moviebooking.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CityRepository extends JpaRepository<City, Long> {
    List<City> findTop6ByCountryIgnoreCaseOrderByDisplayOrderAsc(String country);

    long countByCountryIgnoreCase(String country);
}
