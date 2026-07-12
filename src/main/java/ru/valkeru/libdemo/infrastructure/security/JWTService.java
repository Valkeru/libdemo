package ru.valkeru.libdemo.infrastructure.security;

import org.springframework.security.core.userdetails.UserDetails;
import ru.valkeru.libdemo.model.dto.security.TokenPayload;
import ru.valkeru.libdemo.domain.entity.user.Token;
import ru.valkeru.libdemo.domain.entity.user.User;

import java.util.UUID;

/**
 * This service is used to implement JWT token handling logic
 */
public interface JWTService {

    /**
     * Principal info and theirs roles
     */
    TokenPayload getPayload(String token);

    boolean isValidToken(String token);

    /**
     * Generate token and store into a database
     *
     * @param userDetails User details
     * @param user User object (typically should be a reference)
     * @return Generated token
     */
    Token generateToken(UserDetails userDetails, User user);

    /**
     * Get a jwt using refresh token
     * @param refreshToken Valid refresh token
     *
     * @return Token entity or null, if token wasn't found
     * (refresh is expired, token is rejected or already rotated)
     */
    Token getByRefreshToken(String refreshToken);

    /**
     * Token rotation
     * @param token Token to refresh
     */
    Token refreshToken(Token token);

    /**
     * Returns a jwt string
     * @param id id токена
     */
    String getJwtById(UUID id);

    String getRefreshTokenById(UUID id);

    void deleteToken(UUID id);

    UUID getTokenId(String token);

    /**
     * Remove all user's token except current token
     */
    void deleteAllTokensExceptPresent(User user, String currentToken);

    void deleteExpiredTokens();

    /**
     * Extract user's id from token
     */
    UUID getUserId(String token);
}
