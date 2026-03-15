package ru.valkeru.libdemo.utility;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.valkeru.libdemo.model.entity.user.Token;
import ru.valkeru.libdemo.model.entity.user.User;
import ru.valkeru.libdemo.service.core.security.JWTService;
import ru.valkeru.libdemo.service.core.security.UserService;

/**
 * Генератор токенов пользователей в тестах
 */
@Component
@RequiredArgsConstructor
public class JwtUtility {

    private Token librarianToken = null;
    private Token adminToken = null;
    private Token userToken = null;

    private final UserService userService;
    private final JWTService jwtService;

    public String librarianToken() {
        if (isInvalidToken(librarianToken)) {
            librarianToken = getToken("default_librarian");
        }

        return librarianToken.getJwt();
    }

    public String adminToken() {
        if (isInvalidToken(adminToken)) {
            adminToken = getToken("default_admin");
        }

        return adminToken.getJwt();
    }

    public String userToken() {
        if (isInvalidToken(userToken)) {
            userToken = getToken("default_user");
        }

        return userToken.getJwt();
    }

    private Token getToken(String userName) {
        UserDetails userDetails = userService.loadUserByUsername(userName);
        User user = userService.getReferenceByUsername(userName);
        return jwtService.generateToken(userDetails, user);
    }

    private boolean isInvalidToken(Token token) {
        return token == null || !jwtService.isValidToken(token.getJwt());
    }
}
