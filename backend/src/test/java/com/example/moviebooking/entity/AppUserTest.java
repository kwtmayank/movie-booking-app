package com.example.moviebooking.entity;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class AppUserTest {

    @Test
    void settersAndGettersWorkAndLifecycleCallbacksSetTimestamps() {
        AppUser user = new AppUser();
        user.setGoogleSub("sub-1");
        user.setEmail("entity@example.com");
        user.setName("Entity User");
        user.setPictureUrl("https://entity-pic");

        user.onCreate();
        Instant createdAt = user.getCreatedAt();
        Instant updatedAt = user.getUpdatedAt();

        assertThat(user.getGoogleSub()).isEqualTo("sub-1");
        assertThat(user.getEmail()).isEqualTo("entity@example.com");
        assertThat(user.getName()).isEqualTo("Entity User");
        assertThat(user.getPictureUrl()).isEqualTo("https://entity-pic");
        assertThat(createdAt).isNotNull();
        assertThat(updatedAt).isNotNull();
        assertThat(updatedAt).isEqualTo(createdAt);

        user.onUpdate();
        assertThat(user.getUpdatedAt()).isAfterOrEqualTo(createdAt);
    }
}
