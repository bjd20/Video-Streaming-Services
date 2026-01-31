package com.videostreaming.gateway.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;   // "your-256-bit-secret-key..."

    @Value("${jwt.expiration}")
    private Long expiration;

    //  Secret string into a Cryptographic Key
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    // Method to generate JWT
    public String generateToken(Long userId, String email, String userName) {

        // Step 1: Create claims (user data to put in token)
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("email", email);
        claims.put("userName", userName);

        // Step 2: Build the JWT
        return Jwts.builder()
                .claims(claims)         // Add user data
                .issuer("https://videostreaming.com")
                .subject(userName)      // Main identifier (userName)
                .issuedAt(new Date())   // When token was created
                .expiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey())   // Sign it with secret key
                .compact();                 // Convert to string
    }

//    Verifies the signature (ensures it wasn't tampered with) & Extracts the payload
    public Claims extractClaims(String token) {
        return Jwts.parser()                                        // Create parser
                .verifyWith(getSigningKey())                        // Use key to verify signature
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public String extractUserName(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isTokenValid(String token) {
        try {
            extractClaims(token);  // Try to parse it
            return !isTokenExpired(token);  // Check if not expired
        } catch (Exception e) {
            return false;  // Invalid signature or malformed
        }
    }

    private boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

}
