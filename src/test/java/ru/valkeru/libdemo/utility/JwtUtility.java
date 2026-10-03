package ru.valkeru.libdemo.utility;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import ru.valkeru.libdemo.domain.entity.user.Token;
import ru.valkeru.libdemo.domain.entity.user.User;
import ru.valkeru.libdemo.infrastructure.security.JWTService;
import ru.valkeru.libdemo.infrastructure.security.UserService;

/**
 * Users tokens generator for tests
 */
@Component
@RequiredArgsConstructor
public class JwtUtility {

    private static final String SCHEME = "Bearer";

    private Token librarianToken = null;
    private Token managerToken = null;
    private Token adminToken = null;
    private Token userToken = null;

    private final UserService userService;
    private final JWTService jwtService;

    public String librarianToken() {
        if (isInvalidToken(librarianToken)) {
            librarianToken = getToken("default_librarian");
        }

        return tokenWithScheme(librarianToken);
    }

    public String managerToken() {
        if (isInvalidToken(managerToken)) {
            managerToken = getToken("manager");
        }

        return tokenWithScheme(managerToken);
    }

    public String adminToken() {
        if (isInvalidToken(adminToken)) {
            adminToken = getToken("admin");
        }

        return tokenWithScheme(adminToken);
    }

    public String userToken() {
        if (isInvalidToken(userToken)) {
            userToken = getToken("default_user");
        }

        return tokenWithScheme(userToken);
    }

    private Token getToken(String userName) {
        UserDetails userDetails = userService.loadUserByUsername(userName);

        return jwtService.generateToken((User) userDetails);
    }

    private boolean isInvalidToken(Token token) {
        return token == null || !jwtService.isValidToken(token.getJwt());
    }

    private static String tokenWithScheme(Token token) {
        return "%s %s".formatted(SCHEME, token.getJwt());
    }
}
