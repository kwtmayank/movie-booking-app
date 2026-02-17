package com.example.moviebooking.service;

import com.example.moviebooking.dto.UpdateProfileRequest;
import com.example.moviebooking.dto.UserProfile;
import com.example.moviebooking.entity.AppUser;
import com.example.moviebooking.repository.AppUserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProfileService {

    private final AppUserRepository appUserRepository;

    public ProfileService(AppUserRepository appUserRepository) {
        this.appUserRepository = appUserRepository;
    }

    @Transactional
    public UserProfile updateProfile(Long userId, UpdateProfileRequest request) {
        AppUser user = appUserRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setDateOfBirth(request.dateOfBirth());
        user.setAddress(trimToNull(request.address()));
        user.setCity(trimToNull(request.city()));
        user.setPhoneNumber(trimToNull(request.phoneNumber()));

        AppUser savedUser = appUserRepository.save(user);
        return mapToUserProfile(savedUser);
    }

    private UserProfile mapToUserProfile(AppUser user) {
        return new UserProfile(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getPictureUrl(),
                user.getDateOfBirth(),
                user.getAddress(),
                user.getCity(),
                user.getPhoneNumber()
        );
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.isEmpty() ? null : trimmed;
    }
}
