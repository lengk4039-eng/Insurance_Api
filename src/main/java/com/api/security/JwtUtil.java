package com.api.security;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

/**
 * Issues and validates the JWTs returned by POST /api/auth/login.
 *
 * IMPORTANT: SECRET is hard-coded here for simplicity in this school
 * project. Before deploying this anywhere real, move it into an
 * environment variable (or application.properties, kept out of git) and
 * never commit a real secret to source control.
 */
@Component
public class JwtUtil {

    private static final String SECRET = "insurance-admin-super-secret-key-change-me-please-32chars-min";
    private static final long EXPIRATION_MS = 24 * 60 * 60 * 1000; // 24 hours

    private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());

    /** Creates a signed token that expires in 24 hours. */
    public String generateToken(String username) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + EXPIRATION_MS);

        return Jwts.builder()
                .subject(username)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    /** True if the token's signature is valid and it has not expired. */
    public boolean isTokenValid(String token) {
        try {
            Jwts.parser().verifyWith(key).build().parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
