package ru.valkeru.libdemo.web.service.base;

import ru.valkeru.libdemo.model.dto.security.TokenDto;
import ru.valkeru.libdemo.model.request.security.SignUpRequest;

public interface SecurityWebService {

    TokenDto performSignIn(SignUpRequest request);

    void revokeTokens(String token);

    TokenDto performTokenRefresh(String refreshToken);
}
