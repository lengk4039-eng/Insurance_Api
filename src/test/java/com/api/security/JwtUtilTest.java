package com.api.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class JwtUtilTest {

    private final JwtUtil jwtUtil = new JwtUtil();

    @Test
    void generatesAValidTokenThatCarriesTheUsername() {
        String token = jwtUtil.generateToken("admin1");

        assertTrue(jwtUtil.isTokenValid(token));
        assertEquals("admin1", jwtUtil.extractUsername(token));
    }

    @Test
    void rejectsATamperedOrGarbageToken() {
        assertFalse(jwtUtil.isTokenValid("not.a.real.token"));
    }
}
