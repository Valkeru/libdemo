package ru.valkeru.libdemo.service.core.security.impl;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.persistence.Tuple;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.valkeru.libdemo.model.entity.user.Token;
import ru.valkeru.libdemo.model.entity.user.User;
import ru.valkeru.libdemo.repository.jpa.user.TokenRepository;
import ru.valkeru.libdemo.service.core.security.JWTService;
import ru.valkeru.libdemo.util.CustomTuple;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class JWTServiceImpl implements JWTService {

    private static final int REFRESH_TOKEN_LENGTH = 32;

    private final TokenRepository tokenRepository;
    private final String jwtSecret;

    /**
     * Время жизни jwt в секундах
     */
    private final Long jwtLifetime;

    /**
     * Время жизни refresh токена в секундах
     */
    private final Long refreshLifetime;

    public JWTServiceImpl(TokenRepository tokenRepository,
                          @Value("${app.security.jwt.secret}") String jwtSecret,
                          @Value("${app.security.jwt.lifetime}") Long jwtLifetime,
                          @Value("${app.security.jwt.refresh-lifetime}") Long refreshLifetime) {
        if (StringUtils.isBlank(jwtSecret) || jwtSecret.getBytes(StandardCharsets.UTF_8).length < 32) {
            throw new  IllegalStateException("JWT secret is too weak, must be at least 256 bits");
        }

        this.tokenRepository = tokenRepository;
        this.jwtSecret = jwtSecret;
        this.jwtLifetime = jwtLifetime;
        this.refreshLifetime = refreshLifetime;
    }

    @Override
    public Token generateToken(UserDetails user) {
        Instant now = Instant.now();

        Date iat = Date.from(now);
        Date exp = Date.from(now.plus(Duration.ofSeconds(jwtLifetime)));

        UUID id = UUID.randomUUID();

        String jwt = Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(iat)
                .expiration(exp)
                .claim("role", ((User) user).getRole())
                .claim("id", id.toString())
                .signWith(getJwtSigningKey())
                .compact();

        Token token = Token.builder()
                .id(id)
                .jwt(jwt)
                .refreshToken(getRefreshToken())
                .refreshTokenExpiry(now.plus(Duration.ofSeconds(refreshLifetime)))
                .user((User) user)
                .build();

        return tokenRepository.persist(token);
    }

    @Override
    public Token getByRefreshToken(String refreshToken) {
        return tokenRepository.getNotExpired(refreshToken, Instant.now());
    }

    @CacheEvict(cacheManager = "jwtCacheManager", cacheNames = "token", key = "#token.id", beforeInvocation = true)
    @Override
    public Token refreshToken(Token token) {
        String rotatingJwt = token.getJwt();

        Claims payload = getPayloadAllowExpired(rotatingJwt);

        Instant now = Instant.now();
        Date iat = Date.from(now);
        Date exp = Date.from(now.plus(Duration.ofSeconds(jwtLifetime)));

        String newJwt = Jwts.builder()
                .subject(payload.getSubject())
                .issuedAt(iat)
                .expiration(exp)
                .claim("role", payload.get("role"))
                .claim("id", token.getId().toString())
                .signWith(getJwtSigningKey())
                .compact();
        String newRefresh = getRefreshToken();

        Instant refreshTokenExpiry = now.plus(Duration.ofSeconds(refreshLifetime));

        token.setJwt(newJwt);
        token.setRefreshToken(newRefresh);
        token.setRefreshTokenExpiry(refreshTokenExpiry);

        Token updated = tokenRepository.merge(token);
        tokenRepository.flush();

        return updated;
    }

    @Cacheable(cacheManager = "jwtCacheManager", cacheNames = "token", key = "#id", unless = "#result == null")
    @Override
    public String getJwtById(UUID id) {
        return tokenRepository.getJwtById(id);
    }

    @Override
    public String getRefreshTokenById(UUID id) {
        return tokenRepository.getRefreshTokenById(id);
    }

    @CacheEvict(cacheManager = "jwtCacheManager", cacheNames = "token", key = "#id")
    @Override
    public void deleteToken(UUID id) {
        tokenRepository.deleteById(id);
    }

    @Override
    public UUID getTokenId(String token) {
        JwtParser parser = getJwtParser();

        Jws<Claims> claimsJws = parser.parseSignedClaims(token);
        return UUID.fromString((String) claimsJws.getPayload().get("id"));
    }

    @Override
    public List<Token> getAllTokensExceptPresent(User user, UUID tokenId) {
        return tokenRepository.getTokensByUserAndIdNot(user, tokenId);
    }

    @Override
    public Tuple getPayload(String token) {
        JwtParser jwtParser = getJwtParser();

        Jws<Claims> claimsJws = jwtParser.parseSignedClaims(token);
        Claims payload = claimsJws.getPayload();

        return CustomTuple.of("subject", payload.getSubject(), "role", payload.get("role"));
    }

    @Override
    public boolean isValidToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return false;
        }

        JwtParser jwtParser = getJwtParser();

        try {
            jwtParser.parseSignedClaims(token);

            return true;
        } catch (JwtException je) {
            return false;
        }
    }

    @Override
    public void deleteExpiredTokens() {
        tokenRepository.deleteExpired(Instant.now());
    }

    @Override
    public String getUserName(String token) {
        JwtParser jwtParser = getJwtParser();

        Jws<Claims> claimsJws = jwtParser.parseSignedClaims(token);
        return claimsJws.getPayload().getSubject();
    }

    private SecretKey getJwtSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    private Claims getPayloadAllowExpired(String jwt) {
        JwtParser jwtParser = getJwtParser();

        try {
            return jwtParser.parseSignedClaims(jwt).getPayload();
        } catch (ExpiredJwtException eje) {
            return eje.getClaims();
        }
    }

    private String getRefreshToken() {
        return RandomStringUtils.secure().nextAlphanumeric(REFRESH_TOKEN_LENGTH);
    }

    private JwtParser getJwtParser() {
        return Jwts.parser()
                .verifyWith(getJwtSigningKey())
                .build();
    }
}
