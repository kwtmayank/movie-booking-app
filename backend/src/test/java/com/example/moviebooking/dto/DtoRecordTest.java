package com.example.moviebooking.dto;

import com.example.moviebooking.service.GoogleUserInfo;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class DtoRecordTest {

    @Test
    void authResponseRecordStoresValues() {
        UserProfile userProfile = new UserProfile(1L, "user@example.com", "User", "https://pic");
        AuthResponse authResponse = new AuthResponse("token", "Bearer", 3600L, userProfile);

        assertThat(authResponse.accessToken()).isEqualTo("token");
        assertThat(authResponse.tokenType()).isEqualTo("Bearer");
        assertThat(authResponse.expiresIn()).isEqualTo(3600L);
        assertThat(authResponse.user()).isEqualTo(userProfile);
    }

    @Test
    void errorResponseRecordStoresValues() {
        Instant now = Instant.now();
        ErrorResponse response = new ErrorResponse(now, 400, "Bad Request", "Invalid input", "/api/path");

        assertThat(response.timestamp()).isEqualTo(now);
        assertThat(response.status()).isEqualTo(400);
        assertThat(response.error()).isEqualTo("Bad Request");
        assertThat(response.message()).isEqualTo("Invalid input");
        assertThat(response.path()).isEqualTo("/api/path");
    }

    @Test
    void googleLoginRequestAndUserProfileRecordsStoreValues() {
        GoogleLoginRequest loginRequest = new GoogleLoginRequest("id-token");
        UserProfile userProfile = new UserProfile(9L, "profile@example.com", "Profile User", null);

        assertThat(loginRequest.idToken()).isEqualTo("id-token");
        assertThat(userProfile.id()).isEqualTo(9L);
        assertThat(userProfile.email()).isEqualTo("profile@example.com");
        assertThat(userProfile.name()).isEqualTo("Profile User");
        assertThat(userProfile.pictureUrl()).isNull();
    }

    @Test
    void googleUserInfoRecordStoresValues() {
        GoogleUserInfo googleUserInfo = new GoogleUserInfo("sub", "email@example.com", "Google User", "https://pic", true);

        assertThat(googleUserInfo.googleSub()).isEqualTo("sub");
        assertThat(googleUserInfo.email()).isEqualTo("email@example.com");
        assertThat(googleUserInfo.name()).isEqualTo("Google User");
        assertThat(googleUserInfo.pictureUrl()).isEqualTo("https://pic");
        assertThat(googleUserInfo.emailVerified()).isTrue();
    }
}
