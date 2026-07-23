package ru.valkeru.libdemo.application;

import ru.valkeru.libdemo.model.dto.security.LibraryPrincipal;
import ru.valkeru.libdemo.model.dto.internal.TokenDto;
import ru.valkeru.libdemo.model.request.security.SignUpRequest;
import ru.valkeru.libdemo.security.Permission;
import ru.valkeru.libdemo.security.Role;

import java.util.Set;

public interface SecurityApplicationService {

    TokenDto performSignUp(SignUpRequest request);

    TokenDto performSignIn(SignUpRequest request);

    LibraryPrincipal authenticate(String jwt);

    void deleteToken(String token);

    void revokeTokens(String token);

    TokenDto performTokenRefresh(String refreshToken);

    void deleteExpiredTokens();

    void updatePermissions(Role role, Set<Permission> permissions);

    Set<Permission> getPermissions(Role role);
}
