package com.example.moviebooking.dto;

public record AuthResponse(
        String accessToken,
        String tokenType,
        long expiresIn,
        UserProfile user
) {
}
