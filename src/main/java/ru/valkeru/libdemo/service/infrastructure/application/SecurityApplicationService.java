package ru.valkeru.libdemo.service.infrastructure.application;

import org.jspecify.annotations.Nullable;
import ru.valkeru.libdemo.model.dto.security.LibraryPrincipal;
import ru.valkeru.libdemo.model.dto.security.TokenDto;
import ru.valkeru.libdemo.model.request.security.SignUpRequest;

public interface SecurityApplicationService {

    TokenDto performSignIn(SignUpRequest request);

    @Nullable
    LibraryPrincipal authenticate(String jwt);

    void deleteToken(String token);

    void revokeTokens(String token);

    TokenDto performTokenRefresh(String refreshToken);

    void deleteExpiredTokens();
}
