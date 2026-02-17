package com.example.moviebooking.repository;

import com.example.moviebooking.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByGoogleSub(String googleSub);
}
