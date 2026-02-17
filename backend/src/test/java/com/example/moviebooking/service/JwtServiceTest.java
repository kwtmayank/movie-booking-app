package com.example.moviebooking.service;

import com.example.moviebooking.entity.AppUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    @Test
    void generateTokenWithBase64SecretContainsExpectedClaims() {
        String rawSecret = "01234567890123456789012345678901";
        String base64Secret = Base64.getEncoder().encodeToString(rawSecret.getBytes(StandardCharsets.UTF_8));
        JwtService jwtService = new JwtService(base64Secret, 600);

        AppUser user = new AppUser();
        ReflectionTestUtils.setField(user, "id", 99L);
        user.setEmail("jane@example.com");
        user.setName("Jane");

        String token = jwtService.generateToken(user);

        Claims claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(base64Secret)))
                .build()
                .parseSignedClaims(token)
                .getPayload();

        assertThat(claims.getSubject()).isEqualTo("99");
        assertThat(claims.get("email", String.class)).isEqualTo("jane@example.com");
        assertThat(claims.get("name", String.class)).isEqualTo("Jane");
        assertThat(claims.getExpiration()).isAfter(claims.getIssuedAt());
    }

    @Test
    void generateTokenWithPlainSecretFallsBackToUtf8Key() {
        String plainSecret = "abcdefghijklmnopqrstuvwxyz123456";
        JwtService jwtService = new JwtService(plainSecret, 300);

        AppUser user = new AppUser();
        ReflectionTestUtils.setField(user, "id", 5L);
        user.setEmail("plain@example.com");
        user.setName("Plain");

        String token = jwtService.generateToken(user);

        Claims claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(plainSecret.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(token)
                .getPayload();

        assertThat(claims.getSubject()).isEqualTo("5");
        assertThat(jwtService.getExpiresInSeconds()).isEqualTo(300L);
    }
}
