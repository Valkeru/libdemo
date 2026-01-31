package ru.valkeru.libdemo.service.application;

import ru.valkeru.libdemo.model.dto.security.TokenDto;
import ru.valkeru.libdemo.model.request.security.SignUpRequest;

public interface SecurityApplicationService {

    TokenDto performSignIn(SignUpRequest request);

    void revokeTokens(String token);

    TokenDto performTokenRefresh(String refreshToken);
}
