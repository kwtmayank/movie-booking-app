package com.example.moviebooking.dto;

import java.time.LocalDate;

public record UpdateProfileRequest(
        LocalDate dateOfBirth,
        String address,
        String city,
        String phoneNumber
) {
}
