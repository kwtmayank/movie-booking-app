package com.example.moviebooking.integration;

import com.example.moviebooking.entity.AppUser;
import com.example.moviebooking.repository.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProfileIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppUserRepository appUserRepository;

    @BeforeEach
    void setUp() {
        appUserRepository.deleteAll();
    }

    @Test
    void updateProfileUpdatesUserInDatabase() throws Exception {
        AppUser user = new AppUser();
        user.setGoogleSub("sub-profile");
        user.setEmail("profile@example.com");
        user.setName("Profile User");
        user.setPictureUrl("https://picture/profile");
        user = appUserRepository.save(user);

        mockMvc.perform(put("/api/profile/users/{userId}", user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "dateOfBirth": "1994-03-15",
                                  "address": "221B Baker Street",
                                  "city": "Pune",
                                  "phoneNumber": "+91-9876543210"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.dateOfBirth").value("1994-03-15"))
                .andExpect(jsonPath("$.address").value("221B Baker Street"))
                .andExpect(jsonPath("$.city").value("Pune"))
                .andExpect(jsonPath("$.phoneNumber").value("+91-9876543210"));

        AppUser updated = appUserRepository.findById(user.getId()).orElseThrow();
        assertThat(updated.getDateOfBirth()).isEqualTo(LocalDate.of(1994, 3, 15));
        assertThat(updated.getAddress()).isEqualTo("221B Baker Street");
        assertThat(updated.getCity()).isEqualTo("Pune");
        assertThat(updated.getPhoneNumber()).isEqualTo("+91-9876543210");
    }

    @Test
    void updateProfileReturnsNotFoundWhenUserDoesNotExist() throws Exception {
        mockMvc.perform(put("/api/profile/users/{userId}", 99999L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                  "dateOfBirth": "1994-03-15",
                                  "address": "Address",
                                  "city": "Pune",
                                  "phoneNumber": "+91-9876543210"
                                }
                                """))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("User not found"));
    }
}
