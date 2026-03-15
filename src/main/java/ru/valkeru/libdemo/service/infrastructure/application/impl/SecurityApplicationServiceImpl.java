package ru.valkeru.libdemo.service.infrastructure.application.impl;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.valkeru.libdemo.model.dto.security.LibraryPrincipal;
import ru.valkeru.libdemo.model.dto.security.TokenDto;
import ru.valkeru.libdemo.model.dto.security.TokenPayload;
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

        String username = user.getUsername();
        if (!userService.isValidPassword(user, request.getPassword())) {
            throw new BadCredentialsException("Invalid password for user %s".formatted(username));
        }

        User referenceByUsername = userService.getReferenceByUsername(username);
        Token token = jwtService.generateToken(user, referenceByUsername);

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

            TokenPayload payload = jwtService.getPayload(actualJwt);

            return new LibraryPrincipal(
                payload.subject(),
                getAuthorities(payload.authorities())
            );
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

    @Transactional
    @Override
    public void revokeTokens(String currentToken) {
        String userName = jwtService.getUserName(currentToken);

        User user = userService.getReferenceByUsername(userName);
        jwtService.deleteAllTokensExceptPresent(user, currentToken);
    }

    @Transactional
    @Override
    public void deleteExpiredTokens() {
        jwtService.deleteExpiredTokens();
    }

    private static List<? extends GrantedAuthority> getAuthorities(List<String> authorities) {
        return authorities.stream()
            .map(SimpleGrantedAuthority::new)
            .toList();
    }
}
