package com.example.moviebooking.integration;

import com.example.moviebooking.entity.AppUser;
import com.example.moviebooking.exception.InvalidGoogleTokenException;
import com.example.moviebooking.repository.AppUserRepository;
import com.example.moviebooking.service.GoogleTokenVerifierService;
import com.example.moviebooking.service.GoogleUserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppUserRepository appUserRepository;

    @BeforeEach
    void setUp() {
        appUserRepository.deleteAll();
        StubGoogleTokenVerifierService.reset();
    }

    @Test
    void loginSuccessCreatesUserAndReturnsToken() throws Exception {
        StubGoogleTokenVerifierService.userInfo = new GoogleUserInfo(
                "sub-success",
                "success@example.com",
                "Success User",
                "https://picture/success",
                true
        );

        mockMvc.perform(post("/api/auth/google/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idToken\":\"valid-token\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").isString())
                .andExpect(jsonPath("$.tokenType").value("Bearer"))
                .andExpect(jsonPath("$.expiresIn").value(3600))
                .andExpect(jsonPath("$.user.email").value("success@example.com"))
                .andExpect(jsonPath("$.user.name").value("Success User"));

        AppUser savedUser = appUserRepository.findByGoogleSub("sub-success").orElseThrow();
        assertThat(savedUser.getEmail()).isEqualTo("success@example.com");
        assertThat(savedUser.getName()).isEqualTo("Success User");
        assertThat(savedUser.getPictureUrl()).isEqualTo("https://picture/success");
    }

    @Test
    void loginSuccessUpdatesExistingUserWhenGoogleSubAlreadyExists() throws Exception {
        StubGoogleTokenVerifierService.userInfo = new GoogleUserInfo(
                "sub-existing",
                "first@example.com",
                "First Name",
                "https://picture/first",
                true
        );
        mockMvc.perform(post("/api/auth/google/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idToken\":\"first-token\"}"))
                .andExpect(status().isOk());

        StubGoogleTokenVerifierService.userInfo = new GoogleUserInfo(
                "sub-existing",
                "updated@example.com",
                "Updated Name",
                "https://picture/updated",
                true
        );
        mockMvc.perform(post("/api/auth/google/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idToken\":\"second-token\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.user.email").value("updated@example.com"))
                .andExpect(jsonPath("$.user.name").value("Updated Name"))
                .andExpect(jsonPath("$.user.pictureUrl").value("https://picture/updated"));

        AppUser savedUser = appUserRepository.findByGoogleSub("sub-existing").orElseThrow();
        assertThat(savedUser.getEmail()).isEqualTo("updated@example.com");
        assertThat(savedUser.getName()).isEqualTo("Updated Name");
        assertThat(savedUser.getPictureUrl()).isEqualTo("https://picture/updated");
        assertThat(appUserRepository.count()).isEqualTo(1L);
    }

    @Test
    void loginValidationFailureReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/auth/google/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("idToken is required"));
    }

    @Test
    void loginReturnsUnauthorizedForInvalidTokenException() throws Exception {
        StubGoogleTokenVerifierService.error = new InvalidGoogleTokenException("Invalid Google ID token");

        mockMvc.perform(post("/api/auth/google/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idToken\":\"invalid-token\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Invalid Google ID token"));
    }

    @Test
    void loginReturnsUnauthorizedWhenEmailNotVerified() throws Exception {
        StubGoogleTokenVerifierService.userInfo = new GoogleUserInfo(
                "sub-unverified",
                "user@example.com",
                "Unverified User",
                "https://picture/unverified",
                false
        );

        mockMvc.perform(post("/api/auth/google/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idToken\":\"token\"}"))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.message").value("Google account email is not verified"));
    }

    @Test
    void loginReturnsInternalServerErrorForUnexpectedFailure() throws Exception {
        StubGoogleTokenVerifierService.error = new RuntimeException("Unexpected");

        mockMvc.perform(post("/api/auth/google/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"idToken\":\"token\"}"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("Unexpected error"));
    }

    @TestConfiguration
    static class StubGoogleVerifierConfig {
        @Bean
        @Primary
        GoogleTokenVerifierService googleTokenVerifierService() {
            return new StubGoogleTokenVerifierService();
        }
    }

    static class StubGoogleTokenVerifierService extends GoogleTokenVerifierService {
        static GoogleUserInfo userInfo = new GoogleUserInfo(
                "sub-default",
                "default@example.com",
                "Default User",
                "https://picture/default",
                true
        );
        static RuntimeException error;

        StubGoogleTokenVerifierService() {
            super("test-client-id");
        }

        static void reset() {
            userInfo = new GoogleUserInfo(
                    "sub-default",
                    "default@example.com",
                    "Default User",
                    "https://picture/default",
                    true
            );
            error = null;
        }

        @Override
        public GoogleUserInfo verify(String idToken) {
            if (error != null) {
                throw error;
            }
            return userInfo;
        }
    }
}
