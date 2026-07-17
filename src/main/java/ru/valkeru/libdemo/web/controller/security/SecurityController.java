package ru.valkeru.libdemo.web.controller.security;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.valkeru.libdemo.config.api.ApiConfig;
import ru.valkeru.libdemo.model.dto.internal.TokenDto;
import ru.valkeru.libdemo.model.request.security.SignUpRequest;
import ru.valkeru.libdemo.application.SecurityApplicationService;
import ru.valkeru.libdemo.security.Permission;
import ru.valkeru.libdemo.security.Role;
import ru.valkeru.libdemo.util.AuthenticationUtil;
import ru.valkeru.libdemo.web.api.security.SecurityApi;

import java.util.Set;

@RestController
@RequiredArgsConstructor
public class SecurityController implements SecurityApi {

    private final SecurityApplicationService securityApplicationService;

    @Override
    public ResponseEntity<Void> signUp(SignUpRequest signUpRequest) {
        TokenDto tokenDto = securityApplicationService.performSignUp(signUpRequest);

        return getTokenResponse(tokenDto);
    }

    @Override
    public ResponseEntity<Void> signIn(SignUpRequest request) {
        TokenDto tokenDto = securityApplicationService.performSignIn(request);

        return getTokenResponse(tokenDto);
    }

    @Override
    public ResponseEntity<Void> refreshToken(String refreshToken) {
        TokenDto tokenDto = securityApplicationService.performTokenRefresh(refreshToken);

        return getTokenResponse(tokenDto);
    }

    @Override
    public ResponseEntity<Void> revokeTokens(HttpServletRequest request) {
        securityApplicationService.revokeTokens(AuthenticationUtil.extractToken(request));

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> updateRolePermissions(Role role, Set<Permission> permissions) {
        securityApplicationService.updatePermissions(role, permissions);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Set<Permission>> getRolePermissions(Role role) {
        Set<Permission> permissions = securityApplicationService.getPermissions(role);

        return ResponseEntity.ok(permissions);
    }

    private ResponseEntity<Void> getTokenResponse(TokenDto tokenDto) {
        return ResponseEntity.noContent()
            .header(ApiConfig.ACCESS_TOKEN_HEADER_NAME, tokenDto.getAccessToken())
            .header(ApiConfig.REFRESH_TOKEN_HEADER_NAME, tokenDto.getRefreshToken())
            .build();
    }
}
