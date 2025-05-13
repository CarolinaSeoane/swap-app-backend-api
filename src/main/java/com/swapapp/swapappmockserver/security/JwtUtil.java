package com.swapapp.swapappmockserver.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;

@Component
public class JwtUtil {

    // Clave secreta de mínimo 32 caracteres (para HS256)
    private static final String SECRET_KEY = "super-secret-key-para-jwt-de-32-chars!";

    // Expiración de 3 días
    private static final long EXPIRATION_TIME_MS = 3 * 24 * 60 * 60 * 1000L;

    public String generateToken(String email) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                .signWith(
                        Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)),
                        SignatureAlgorithm.HS256
                )
                .compact();
    }

    public String extractEmail(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public boolean validateToken(String token) {
        try {
            extractEmail(token); // Si esto falla, es inválido
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
