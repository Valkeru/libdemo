package ru.valkeru.libdemo.service.infrastructure.application.impl;

import io.jsonwebtoken.JwtException;
import jakarta.persistence.Tuple;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.valkeru.libdemo.config.security.LibraryPrincipal;
import ru.valkeru.libdemo.model.dto.security.TokenDto;
import ru.valkeru.libdemo.model.entity.user.Token;
import ru.valkeru.libdemo.model.entity.user.User;
import ru.valkeru.libdemo.model.request.security.SignUpRequest;
import ru.valkeru.libdemo.service.core.security.JWTService;
import ru.valkeru.libdemo.service.core.security.UserService;
import ru.valkeru.libdemo.service.infrastructure.application.SecurityApplicationService;

import java.util.List;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityApplicationServiceImpl implements SecurityApplicationService {

    private final UserService userService;
    private final JWTService jwtService;

    @Override
    public TokenDto performSignIn(SignUpRequest request) {
        UserDetails user = userService.loadUserByUsername(request.getUsername());

        if (!userService.isValidPassword(user, request.getPassword())) {
            throw new BadCredentialsException("Invalid password for user %s".formatted(user.getUsername()));
        }

        Token token = jwtService.generateToken(user);

        return TokenDto.builder()
                .accessToken(token.getJwt())
                .refreshToken(token.getRefreshToken())
                .build();
    }

    @Override
    public LibraryPrincipal authenticate(String jwt) {
        if (StringUtils.isBlank(jwt)) {
            return null;
        }

        try {
            UUID tokenId = jwtService.getTokenId(jwt);
            String actualJwt = jwtService.getJwtById(tokenId);

            if (!jwt.equals(actualJwt)) {
                return null;
            }

            Tuple payload = jwtService.getPayload(actualJwt);

            return new LibraryPrincipal(payload.get("subject", String.class), payload.get("role", String.class));
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Invalid JWT: {}", e.getMessage());

            return null;
        } catch (Exception e) {
            log.error("Failed to authenticate user", e);

            return null;
        }
    }

    @Transactional
    @Override
    public TokenDto performTokenRefresh(String refreshToken) {
        Token token = jwtService.getByRefreshToken(refreshToken);

        if (token == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }

        Token refreshed = jwtService.refreshToken(token);

        return TokenDto.builder()
                .accessToken(refreshed.getJwt())
                .refreshToken(refreshed.getRefreshToken())
                .build();
    }

    @Override
    public void deleteToken(String token) {
        UUID tokenId = jwtService.getTokenId(token);
        jwtService.deleteToken(tokenId);
    }

    @Override
    public void revokeTokens(String currentToken) {

        String userName = jwtService.getUserName(currentToken);

        User user = (User) userService.loadUserByUsername(userName);
        UUID tokenId = jwtService.getTokenId(currentToken);

        List<Token> tokensToRevoke = jwtService.getAllTokensExceptPresent(user, tokenId);

        for (Token token: tokensToRevoke) {
            jwtService.deleteToken(token.getId());
        }
    }
}
