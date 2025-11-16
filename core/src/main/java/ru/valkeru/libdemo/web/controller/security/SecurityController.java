package ru.valkeru.libdemo.web.controller.security;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.valkeru.libdemo.model.dto.security.TokenDto;
import ru.valkeru.libdemo.model.request.security.SignUpRequest;
import ru.valkeru.libdemo.web.service.base.SecurityWebService;
import ru.valkeru.libdemo.web.api.security.SecurityApi;

@Component
@RequiredArgsConstructor
public class SecurityController implements SecurityApi {

    private final SecurityWebService securityWebService;

    @Override
    public ResponseEntity<Void> signUp(SignUpRequest signUpRequest) {
        return null;
    }

    @Override
    public ResponseEntity<TokenDto> signIn(SignUpRequest request) {
        return ResponseEntity.ok(securityWebService.performSignIn(request));
    }

    @Override
    public ResponseEntity<TokenDto> refreshToken(String refreshToken) {
        return ResponseEntity.ok(securityWebService.performTokenRefresh(refreshToken));
    }

    @Override
    public ResponseEntity<Void> revokeTokens(String token) {
        securityWebService.revokeTokens(token);

        return ResponseEntity.noContent().build();
    }
}
