package ru.valkeru.libdemo.application.impl;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.valkeru.libdemo.infrastructure.security.PermissionService;
import ru.valkeru.libdemo.model.dto.security.LibraryPrincipal;
import ru.valkeru.libdemo.model.dto.security.LibraryUser;
import ru.valkeru.libdemo.model.dto.internal.TokenDto;
import ru.valkeru.libdemo.model.dto.security.TokenPayload;
import ru.valkeru.libdemo.persistence.entity.user.Token;
import ru.valkeru.libdemo.persistence.entity.user.User;
import ru.valkeru.libdemo.model.request.security.SignUpRequest;
import ru.valkeru.libdemo.infrastructure.security.JWTService;
import ru.valkeru.libdemo.infrastructure.security.UserService;
import ru.valkeru.libdemo.application.SecurityApplicationService;
import ru.valkeru.libdemo.security.Permission;
import ru.valkeru.libdemo.security.Role;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityApplicationServiceImpl implements SecurityApplicationService {

    private final UserService userService;
    private final JWTService jwtService;
    public final PermissionService permissionService;

    @Transactional
    public TokenDto performSignUp(SignUpRequest signUpRequest) {
        userService.createUser(signUpRequest, Role.ROLE_USER);

        return performSignIn(signUpRequest);
    }

    @Override
    public TokenDto performSignIn(SignUpRequest request) {
        UserDetails user = userService.loadUserByUsername(request.getUsername());

        String username = user.getUsername();
        if (!userService.isValidPassword(user, request.getPassword())) {
            throw new BadCredentialsException("Invalid password for user %s".formatted(username));
        }

        User referenceByUsername = userService.getReference(((LibraryUser) user).id());
        Token token = jwtService.generateToken(user, referenceByUsername);

        return buildTokenDto(token);
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

            Role role = payload.role();

            return new LibraryPrincipal(
                payload.userId(),
                payload.subject(),
                role,
                getAuthoritiesForRole(role)
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

        return buildTokenDto(refreshed);
    }

    @Override
    @Transactional
    public void deleteToken(String token) {
        UUID tokenId = jwtService.getTokenId(token);
        jwtService.deleteToken(tokenId);
    }

    @Transactional
    @Override
    public void revokeTokens(String currentToken) {
        UUID id = jwtService.getUserId(currentToken);
        User user = userService.getReference(id);

        jwtService.deleteAllTokensExceptPresent(user, currentToken);
    }

    @Transactional
    @Override
    public void deleteExpiredTokens() {
        jwtService.deleteExpiredTokens();
    }

    @Override
    @Transactional
    public void updatePermissions(Role role, Set<Permission> permissions) {
        if (Role.ROLE_ADMIN.equals(role)) {
            // Admin has all permissions. They are managed by application and not stored in database
            return;
        }

        permissionService.replacePermissions(role, permissions);
    }

    @Override
    public Set<Permission> getPermissions(Role role) {
        if (Role.ROLE_ADMIN.equals(role)) {
            return EnumSet.allOf(Permission.class).stream()
                .filter(Permission::isAtomic)
                .collect(Collectors.toSet());
        }

        return new HashSet<>(permissionService.findPermissionsByRole(role));
    }

    private List<SimpleGrantedAuthority> getAuthoritiesForRole(Role role) {
        Collection<Permission> permissions = Role.ROLE_ADMIN == role
            ? EnumSet.allOf(Permission.class).stream()
              .filter(Permission::isAtomic)
              .toList()
            : permissionService.findPermissionsByRole(role);

        Set<String> permissionsNames = permissions.stream()
            .map(Permission::name)
            .collect(Collectors.toSet());

        List<String> result = new ArrayList<>();
        result.add(role.name());
        result.addAll(permissionsNames);

        return result.stream()
            .map(SimpleGrantedAuthority::new)
            .toList();
    }

    private TokenDto buildTokenDto(Token refreshed) {
        return TokenDto.builder()
            .accessToken(refreshed.getJwt())
            .refreshToken(refreshed.getRefreshToken())
            .build();
    }

    private String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }
}
