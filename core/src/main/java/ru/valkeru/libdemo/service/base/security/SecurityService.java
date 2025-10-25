package ru.valkeru.libdemo.service.base.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import ru.valkeru.libdemo.config.security.LibraryPrincipal;
import ru.valkeru.libdemo.model.entity.user.Token;
import ru.valkeru.libdemo.model.entity.user.User;

import java.util.List;
import java.util.UUID;

/**
 * Сервис реализует логику работы с пользователями и jwt
 */
public interface SecurityService extends UserDetailsService {

    boolean isValidToken(String token);

    UserDetails loadUserByToken(String token);

    LibraryPrincipal loadPrincipalFromToken(String token);

    boolean isValidPassword(UserDetails user, String password);

    String hashPassword(String password);

    /**
     * Сгенерировать токен и сохранить в базу
     * @param user Пользователь
     *
     * @return ID сгенерированного токена
     */
    UUID generateToken(UserDetails user);

    UUID getIdForNotExpiredRefreshToken(String refreshToken);

    /**
     * Ротация токена
     * @param id ID токена в БД
     * @return Новый refresh токен
     */
    String refreshToken(UUID id);

    /**
     * Возвращает строку jwt токена
     * @param id id токена
     */
    String getJwtById(UUID id);

    String getRefreshTokenById(UUID id);

    void deleteToken(UUID id);

    UUID getTokenId(String token);

    /**
     * Получить все токены, выпущенные для пользователя, кроме текущего
     */
    List<Token> getAllTokensExceptPresent(User user, UUID tokenId);
}
