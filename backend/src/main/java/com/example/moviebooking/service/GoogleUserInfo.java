package com.example.moviebooking.service;

public record GoogleUserInfo(
        String googleSub,
        String email,
        String name,
        String pictureUrl,
        boolean emailVerified
) {
}
