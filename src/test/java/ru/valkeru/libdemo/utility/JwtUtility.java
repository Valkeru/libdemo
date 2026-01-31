package ru.valkeru.libdemo.utility;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.valkeru.libdemo.security.Role;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.UUID;

/**
 * Генератор токенов пользователей в тестах
 */
@Component
public class JwtUtility {

    private final String jwtSecret;

    public JwtUtility(@Value("${app.security.jwt.secret}") String jwtSecret) {
        this.jwtSecret = jwtSecret;
    }

    public String librarianToken() {
        return Jwts.builder()
                .signWith(getJwtSigningKey())
                .subject("default_librarian")
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(Duration.ofMinutes(1))))
                .claim("id", UUID.randomUUID())
                .claim("role", Role.LIBRARIAN.name())
                .compact();
    }

    public String adminToken() {
        return Jwts.builder()
                .signWith(getJwtSigningKey())
                .subject("admin")
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(Duration.ofMinutes(1))))
                .claim("id", UUID.randomUUID())
                .claim("role", Role.ADMIN.name())
                .compact();
    }

    public String userToken() {
        return Jwts.builder()
                .signWith(getJwtSigningKey())
                .subject("default_user")
                .issuedAt(Date.from(Instant.now()))
                .expiration(Date.from(Instant.now().plus(Duration.ofMinutes(1))))
                .claim("id", UUID.randomUUID())
                .claim("role", Role.USER.name())
                .compact();
    }

    private SecretKey getJwtSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
}
