package com.videostreaming.account.security;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        // @Value fields aren't populated outside a Spring context, so inject manually
        ReflectionTestUtils.setField(jwtUtil, "secret",
                "test-only-secret-key-do-not-use-in-any-real-environment-1234567890");
        ReflectionTestUtils.setField(jwtUtil, "expiration", 3600000L);
    }

    @Test
    void generateToken_thenExtractUserName_returnsOriginalUserName() {
        String token = jwtUtil.generateToken(1L, "alice@example.com", "alice");

        assertThat(jwtUtil.extractUserName(token)).isEqualTo("alice");
    }

    @Test
    void generateToken_containsExpectedClaims() {
        String token = jwtUtil.generateToken(42L, "bob@example.com", "bob");

        Claims claims = jwtUtil.extractClaims(token);

        assertThat(claims.get("userId", Long.class)).isEqualTo(42L);
        assertThat(claims.get("email", String.class)).isEqualTo("bob@example.com");
        assertThat(claims.get("userName", String.class)).isEqualTo("bob");
    }

    @Test
    void isTokenValid_validToken_returnsTrue() {
        String token = jwtUtil.generateToken(1L, "alice@example.com", "alice");

        assertThat(jwtUtil.isTokenValid(token)).isTrue();
    }

    @Test
    void isTokenValid_expiredToken_returnsFalse() {
        // Use a 1ms expiration so the token is immediately expired
        ReflectionTestUtils.setField(jwtUtil, "expiration", 1L);
        String token = jwtUtil.generateToken(1L, "alice@example.com", "alice");

        // small sleep to guarantee expiry has passed
        try { Thread.sleep(10); } catch (InterruptedException ignored) {}

        assertThat(jwtUtil.isTokenValid(token)).isFalse();
    }

    @Test
    void isTokenValid_malformedToken_returnsFalse() {
        assertThat(jwtUtil.isTokenValid("not-a-real-jwt-token")).isFalse();
    }
}