package com.example.moviebooking.service;

import com.example.moviebooking.entity.AppUser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtService {

    private final SecretKey secretKey;
    private final long expiresInSeconds;

    public JwtService(
            @Value("${app.auth.jwt.secret}") String jwtSecret,
            @Value("${app.auth.jwt.expires-in-seconds:3600}") long expiresInSeconds
    ) {
        this.secretKey = buildSigningKey(jwtSecret);
        this.expiresInSeconds = expiresInSeconds;
    }

    public String generateToken(AppUser user) {
        Instant now = Instant.now();
        Instant expiresAt = now.plusSeconds(expiresInSeconds);

        return Jwts.builder()
                .subject(String.valueOf(user.getId()))
                .claim("email", user.getEmail())
                .claim("name", user.getName())
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiresAt))
                .signWith(secretKey)
                .compact();
    }

    public long getExpiresInSeconds() {
        return expiresInSeconds;
    }

    private SecretKey buildSigningKey(String jwtSecret) {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
            return Keys.hmacShaKeyFor(keyBytes);
        } catch (RuntimeException ex) {
            return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
        }
    }
}
