package com.example.moviebooking.service;

import com.example.moviebooking.dto.AuthResponse;
import com.example.moviebooking.dto.UserProfile;
import com.example.moviebooking.entity.AppUser;
import com.example.moviebooking.exception.InvalidGoogleTokenException;
import com.example.moviebooking.repository.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final GoogleTokenVerifierService googleTokenVerifierService;
    private final AppUserRepository appUserRepository;
    private final JwtService jwtService;

    public AuthService(
            GoogleTokenVerifierService googleTokenVerifierService,
            AppUserRepository appUserRepository,
            JwtService jwtService
    ) {
        this.googleTokenVerifierService = googleTokenVerifierService;
        this.appUserRepository = appUserRepository;
        this.jwtService = jwtService;
    }

    @Transactional
    public AuthResponse loginWithGoogle(String idToken) {
        GoogleUserInfo googleUser = googleTokenVerifierService.verify(idToken);

        if (!googleUser.emailVerified()) {
            throw new InvalidGoogleTokenException("Google account email is not verified");
        }

        AppUser user = appUserRepository.findByGoogleSub(googleUser.googleSub())
                .map(existing -> updateExistingUser(existing, googleUser))
                .orElseGet(() -> createUser(googleUser));

        user = appUserRepository.save(user);
        String accessToken = jwtService.generateToken(user);

        return new AuthResponse(
                accessToken,
                "Bearer",
                jwtService.getExpiresInSeconds(),
                new UserProfile(
                        user.getId(),
                        user.getEmail(),
                        user.getName(),
                        user.getPictureUrl(),
                        user.getDateOfBirth(),
                        user.getAddress(),
                        user.getCity(),
                        user.getPhoneNumber()
                )
        );
    }

    private AppUser updateExistingUser(AppUser existing, GoogleUserInfo googleUser) {
        existing.setEmail(googleUser.email());
        existing.setName(googleUser.name());
        existing.setPictureUrl(googleUser.pictureUrl());
        return existing;
    }

    private AppUser createUser(GoogleUserInfo googleUser) {
        AppUser user = new AppUser();
        user.setGoogleSub(googleUser.googleSub());
        user.setEmail(googleUser.email());
        user.setName(googleUser.name());
        user.setPictureUrl(googleUser.pictureUrl());
        return user;
    }
}
