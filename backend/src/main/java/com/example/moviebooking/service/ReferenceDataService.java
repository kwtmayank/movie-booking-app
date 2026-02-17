package com.example.moviebooking.service;

import com.example.moviebooking.entity.City;
import com.example.moviebooking.repository.CityRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReferenceDataService {

    private final CityRepository cityRepository;

    public ReferenceDataService(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }

    public List<String> getTopIndianCities() {
        return cityRepository.findTop6ByCountryIgnoreCaseOrderByDisplayOrderAsc("India").stream()
                .map(City::getName)
                .toList();
    }
}
