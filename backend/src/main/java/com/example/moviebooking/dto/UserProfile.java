package com.example.moviebooking.dto;

public record UserProfile(
        Long id,
        String email,
        String name,
        String pictureUrl
) {
}
