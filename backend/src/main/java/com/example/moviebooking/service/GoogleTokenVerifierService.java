package com.example.moviebooking.service;

import com.example.moviebooking.exception.InvalidGoogleTokenException;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
public class GoogleTokenVerifierService {

    private final GoogleIdTokenVerifier verifier;

    public GoogleTokenVerifierService(@Value("${app.auth.google.client-id}") String clientId) {
        this.verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(clientId))
                .build();
    }

    public GoogleUserInfo verify(String idToken) {
        GoogleIdToken googleIdToken;
        try {
            googleIdToken = verifier.verify(idToken);
        } catch (GeneralSecurityException | IOException | RuntimeException ex) {
            throw new InvalidGoogleTokenException("Unable to verify Google token", ex);
        }

        if (googleIdToken == null) {
            throw new InvalidGoogleTokenException("Invalid Google ID token");
        }

        GoogleIdToken.Payload payload = googleIdToken.getPayload();
        Object emailVerifiedObj = payload.get("email_verified");
        boolean emailVerified = emailVerifiedObj instanceof Boolean && (Boolean) emailVerifiedObj;

        return new GoogleUserInfo(
                payload.getSubject(),
                payload.getEmail(),
                (String) payload.get("name"),
                (String) payload.get("picture"),
                emailVerified
        );
    }
}
