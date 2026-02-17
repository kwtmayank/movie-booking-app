package com.example.moviebooking.entity;

import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class AppUserTest {

    @Test
    void settersAndGettersWorkAndLifecycleCallbacksSetTimestamps() {
        AppUser user = new AppUser();
        user.setGoogleSub("sub-1");
        user.setEmail("entity@example.com");
        user.setName("Entity User");
        user.setPictureUrl("https://entity-pic");
        user.setDateOfBirth(LocalDate.of(1998, 7, 20));
        user.setAddress("Some address");
        user.setCity("Mumbai");
        user.setPhoneNumber("+91-99999");

        user.onCreate();
        Instant createdAt = user.getCreatedAt();
        Instant updatedAt = user.getUpdatedAt();

        assertThat(user.getGoogleSub()).isEqualTo("sub-1");
        assertThat(user.getEmail()).isEqualTo("entity@example.com");
        assertThat(user.getName()).isEqualTo("Entity User");
        assertThat(user.getPictureUrl()).isEqualTo("https://entity-pic");
        assertThat(user.getDateOfBirth()).isEqualTo(LocalDate.of(1998, 7, 20));
        assertThat(user.getAddress()).isEqualTo("Some address");
        assertThat(user.getCity()).isEqualTo("Mumbai");
        assertThat(user.getPhoneNumber()).isEqualTo("+91-99999");
        assertThat(createdAt).isNotNull();
        assertThat(updatedAt).isNotNull();
        assertThat(updatedAt).isEqualTo(createdAt);

        user.onUpdate();
        assertThat(user.getUpdatedAt()).isAfterOrEqualTo(createdAt);
    }
}
