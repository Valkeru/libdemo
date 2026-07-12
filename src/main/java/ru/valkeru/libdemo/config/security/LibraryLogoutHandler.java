package ru.valkeru.libdemo.config.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.NonNull;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import ru.valkeru.libdemo.config.OpenApiConfig;
import ru.valkeru.libdemo.application.SecurityApplicationService;

@Slf4j
@RequiredArgsConstructor
public class LibraryLogoutHandler implements LogoutHandler {

    private final SecurityApplicationService securityApplicationService;

    @Override
    public void logout(HttpServletRequest request, @NonNull HttpServletResponse response, Authentication authentication) {
        String token = request.getHeader(OpenApiConfig.ACCESS_TOKEN_HEADER_NAME);

        if (StringUtils.isBlank(token)) {
            return;
        }

        try {
            securityApplicationService.deleteToken(token);
        } catch (Exception e) {
            log.warn("Failed to logout", e);
        }
    }
}
