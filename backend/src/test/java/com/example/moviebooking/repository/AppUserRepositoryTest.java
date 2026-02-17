package com.example.moviebooking.repository;

import com.example.moviebooking.entity.AppUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository appUserRepository;

    @Test
    void findByGoogleSubReturnsUserWhenExists() {
        AppUser user = new AppUser();
        user.setGoogleSub("google-sub-abc");
        user.setEmail("repo@example.com");
        user.setName("Repo User");
        user.setPictureUrl("https://repo-pic");
        appUserRepository.save(user);

        Optional<AppUser> found = appUserRepository.findByGoogleSub("google-sub-abc");

        assertThat(found).isPresent();
        assertThat(found.get().getEmail()).isEqualTo("repo@example.com");
        assertThat(found.get().getName()).isEqualTo("Repo User");
    }

    @Test
    void findByGoogleSubReturnsEmptyWhenNotExists() {
        Optional<AppUser> found = appUserRepository.findByGoogleSub("missing-sub");
        assertThat(found).isEmpty();
    }
}
