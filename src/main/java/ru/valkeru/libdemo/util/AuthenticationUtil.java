package ru.valkeru.libdemo.util;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;

import java.util.Optional;

@UtilityClass
public class AuthenticationUtil {

    private static final String AUTH_METHOD = "Bearer ";

    public String extractToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(HttpHeaders.AUTHORIZATION))
            .map(header -> header.substring(AUTH_METHOD.length()).trim())
            .orElse(null);
    }
}
