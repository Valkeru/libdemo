package ru.valkeru.libdemo.service.impl.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import ru.valkeru.libdemo.config.security.LibraryPrincipal;
import ru.valkeru.libdemo.model.entity.user.Token;
import ru.valkeru.libdemo.model.entity.user.User;
import ru.valkeru.libdemo.repository.jpa.user.TokenRepository;
import ru.valkeru.libdemo.repository.jpa.user.UserRepository;
import ru.valkeru.libdemo.service.base.security.SecurityService;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class SecurityServiceImpl implements SecurityService {

    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final String jwtSecret;
    private final Long jwtLifetime;
    private final Long refreshLifetime;

    public SecurityServiceImpl(UserRepository userRepository,
                               TokenRepository tokenRepository,
                               @Value("${app.security.jwt.secret}") String jwtSecret,
                               @Value("${app.security.jwt.lifetime}") Long jwtLifetime,
                               @Value("${app.security.jwt.refresh-lifetime}") Long refreshLifetime) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.jwtSecret = jwtSecret;
        this.jwtLifetime = jwtLifetime;
        this.refreshLifetime = refreshLifetime;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User %s not found".formatted(username)));
    }

    @Override
    public boolean isValidPassword(UserDetails user, String password) {
        return BCrypt.checkpw(password, user.getPassword());
    }

    @Override
    public String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    @Override
    @SneakyThrows
    public UUID generateToken(UserDetails user) {
        Date iat = new Date(System.currentTimeMillis());
        UUID id = UUID.randomUUID();

        String jwt = Jwts.builder()
                .subject(user.getUsername())
                .issuedAt(iat)
                .expiration(new Date(iat.getTime() + Duration.ofSeconds(jwtLifetime).toMillis()))
                .claim("role", ((User) user).getRole())
                .claim("id", id)
                .signWith(getJwtSigningKey())
                .compact();


        Token built = Token.builder()
                .id(id)
                .jwt(jwt)
                .refreshToken(RandomStringUtils.secure().nextAlphanumeric(32))
                .refreshTokenExpiry(Instant.now().plus(Duration.ofSeconds(refreshLifetime)))
                .user((User) user)
                .build();

        tokenRepository.save(built);

        return id;
    }

    @Override
    public UUID getIdForNotExpiredRefreshToken(String refreshToken) {
        return tokenRepository.getNotExpiredTokenId(refreshToken);
    }

    @CacheEvict(cacheManager = "jwtCacheManager", cacheNames = "token", key = "#id")
    @Override
    public String refreshToken(UUID id) {
        String rotatingJwt = tokenRepository.getJwtById(id);

        JwtParser jwtParser = Jwts.parser()
                .verifyWith(getJwtSigningKey())
                .build();

        Claims payload;
        try {
            payload = jwtParser.parseSignedClaims(rotatingJwt).getPayload();
        } catch (ExpiredJwtException eje) {
            payload = eje.getClaims();
        }

        Date iat = new Date(System.currentTimeMillis());
        String newJwt = Jwts.builder()
                .subject(payload.getSubject())
                .issuedAt(iat)
                .expiration(new Date(iat.getTime() + Duration.ofSeconds(jwtLifetime).toMillis()))
                .claim("role", payload.get("role"))
                .claim("id", id)
                .signWith(getJwtSigningKey())
                .compact();
        String newRefresh = RandomStringUtils.secure().nextAlphanumeric(32);

        tokenRepository.updateTokenById(id, newJwt, newRefresh, Instant.now().plus(Duration.ofSeconds(refreshLifetime)));

        return newRefresh;
    }

    @Cacheable(cacheManager = "jwtCacheManager", cacheNames = "token", key = "#id")
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
        JwtParser parser = Jwts.parser()
                .verifyWith(getJwtSigningKey())
                .build();

        Jws<Claims> claimsJws = parser.parseSignedClaims(token);
        return UUID.fromString((String) claimsJws.getPayload().get("id"));
    }

    @Override
    public List<Token> getAllTokensExceptPresent(User user, UUID tokenId) {
        return tokenRepository.getTokensByUserAndIdNot(user, tokenId);
    }

    @Override
    public UserDetails loadUserByToken(String token) {
        return loadUserByUsername(getUserName(token));
    }

    @Override
    public LibraryPrincipal loadPrincipalFromToken(String token) {
        JwtParser jwtParser = Jwts.parser()
                .verifyWith(getJwtSigningKey())
                .build();

        Jws<Claims> claimsJws = jwtParser.parseSignedClaims(token);
        Claims payload = claimsJws.getPayload();

        return LibraryPrincipal.builder()
                .userName(payload.getSubject())
                .role((String) payload.get("role"))
                .build();
    }

    @Override
    public boolean isValidToken(String token) {
        if (StringUtils.isEmpty(token)) {
            return false;
        }

        JwtParser jwtParser = Jwts.parser()
                .verifyWith(getJwtSigningKey())
                .build();

        try {
            jwtParser.parseSignedClaims(token);

            return true;
        } catch (JwtException je) {
            return false;
        }
    }

    private String getUserName(String token) {
        JwtParser jwtParser = Jwts.parser()
                .verifyWith(getJwtSigningKey())
                .build();

        Jws<Claims> claimsJws = jwtParser.parseSignedClaims(token);
        return claimsJws.getPayload().getSubject();
    }

    private SecretKey getJwtSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }
}
