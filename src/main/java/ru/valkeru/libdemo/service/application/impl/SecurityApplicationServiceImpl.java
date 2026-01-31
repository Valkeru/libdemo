package ru.valkeru.libdemo.service.application.impl;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;
import ru.valkeru.libdemo.model.dto.security.TokenDto;
import ru.valkeru.libdemo.model.entity.user.Token;
import ru.valkeru.libdemo.model.entity.user.User;
import ru.valkeru.libdemo.model.request.security.SignUpRequest;
import ru.valkeru.libdemo.service.core.SecurityService;
import ru.valkeru.libdemo.service.application.SecurityApplicationService;

import java.util.List;
import java.util.UUID;

@Component
public class SecurityApplicationServiceImpl implements SecurityApplicationService {

    private final SecurityService securityService;


    public SecurityApplicationServiceImpl(SecurityService securityService) {
        this.securityService = securityService;
    }

    @Override
    public TokenDto performSignIn(SignUpRequest request) {
        UserDetails user = securityService.loadUserByUsername(request.getUsername());
        if (!securityService.isValidPassword(user, request.getPassword())) {
            // TODO: throw

            return null;
        }

        UUID id = securityService.generateToken(user);
        String jwt = securityService.getJwtById(id);
        String refresh = securityService.getRefreshTokenById(id);

        return TokenDto.builder().accessToken(jwt).refreshToken(refresh).build();
    }

    @Override
    public TokenDto performTokenRefresh(String refreshToken) {
        UUID id = securityService.getIdForNotExpiredRefreshToken(refreshToken);

        if (id == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid refresh token");
        }

        String newRefresh = securityService.refreshToken(id);
        String jwt = securityService.getJwtById(id);

        return TokenDto.builder()
                .accessToken(jwt)
                .refreshToken(newRefresh)
                .build();
    }

    @Override
    public void revokeTokens(String currentToken) {
        User user = (User) securityService.loadUserByToken(currentToken);
        UUID tokenId = securityService.getTokenId(currentToken);

        List<Token> tokensToRevoke = securityService.getAllTokensExceptPresent(user, tokenId);

        for (Token token: tokensToRevoke) {
            securityService.deleteToken(token.getId());
        }
    }
}
