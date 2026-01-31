package ru.valkeru.libdemo.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import ru.valkeru.libdemo.config.OpenApiConfig;
import ru.valkeru.libdemo.service.core.SecurityService;

import java.util.UUID;

@RequiredArgsConstructor
public class LibraryLogoutHandler implements LogoutHandler {

    private final SecurityService securityService;

    @Override
    @SneakyThrows
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = request.getHeader(OpenApiConfig.ACCESS_TOKEN_HEADER_NAME);

        if (securityService.isValidToken(token)) {
            UUID tokenId = securityService.getTokenId(token);
            securityService.deleteToken(tokenId);
        }

        response.sendRedirect("/");
    }
}
