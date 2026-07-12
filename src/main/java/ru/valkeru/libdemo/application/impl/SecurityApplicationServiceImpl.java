package ru.valkeru.libdemo.application.impl;

import io.jsonwebtoken.JwtException;
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
import ru.valkeru.libdemo.config.security.RolePermissionProperties;
import ru.valkeru.libdemo.model.dto.security.LibraryPrincipal;
import ru.valkeru.libdemo.model.dto.security.LibraryUser;
import ru.valkeru.libdemo.model.dto.security.TokenDto;
import ru.valkeru.libdemo.model.dto.security.TokenPayload;
import ru.valkeru.libdemo.domain.entity.user.Token;
import ru.valkeru.libdemo.domain.entity.user.User;
import ru.valkeru.libdemo.model.request.security.SignUpRequest;
import ru.valkeru.libdemo.security.Permission;
import ru.valkeru.libdemo.security.Role;
import ru.valkeru.libdemo.infrastructure.security.JWTService;
import ru.valkeru.libdemo.infrastructure.security.UserService;
import ru.valkeru.libdemo.application.SecurityApplicationService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
public class SecurityApplicationServiceImpl implements SecurityApplicationService {

    private final Map<Role, List<? extends GrantedAuthority>> roleAuthoritiesMap;
    private final UserService userService;
    private final JWTService jwtService;

    public SecurityApplicationServiceImpl(RolePermissionProperties permissionProperties,
                                          UserService userService, JWTService jwtService) {

        Map<Role, Collection<Permission>> rolePermissionsMap = new HashMap<>(permissionProperties.getPermissions());
        rolePermissionsMap.put(Role.ROLE_ADMIN, EnumSet.allOf(Permission.class));
        Set<Role> roles = new HashSet<>(rolePermissionsMap.keySet());

        Map<Role, List<SimpleGrantedAuthority>> roleAuthorityMap = roles.stream()
            .collect(Collectors.toMap(
                Function.identity(),
                role -> getAuthoritiesForRole(rolePermissionsMap, role)
            ));

        this.userService = userService;
        this.jwtService = jwtService;
        this.roleAuthoritiesMap = Map.copyOf(roleAuthorityMap);
    }

    private static List<SimpleGrantedAuthority> getAuthoritiesForRole(Map<Role, Collection<Permission>> rolePermissionsMap, Role role) {
        Set<Permission> permissions = getPermissions(rolePermissionsMap, role);

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

    private static Set<Permission> getPermissions(Map<Role, Collection<Permission>> rolePermissionsMap, Role role) {
        return rolePermissionsMap.getOrDefault(role, List.of()).stream()
            .flatMap(permission -> permission.resolvePermissions().stream())
            .collect(Collectors.toSet());
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
                payload.userId(),
                payload.subject(),
                payload.role(),
                roleAuthoritiesMap.getOrDefault(payload.role(), List.of())
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
        if (tokenId != null) {
            return;
        }

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
}
