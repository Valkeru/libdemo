package ru.valkeru.libdemo.service.core.security;

import org.springframework.security.core.userdetails.UserDetails;
import ru.valkeru.libdemo.model.dto.security.TokenPayload;
import ru.valkeru.libdemo.model.entity.user.Token;
import ru.valkeru.libdemo.model.entity.user.User;

import java.util.UUID;

/**
 * Сервис реализует логику работы с jwt
 */
public interface JWTService {

    /**
     * Возвращает данные о субъекте и его роли, упакованные в кортеж
     * Ключи кортежа - subject и role
     */
    TokenPayload getPayload(String token);

    boolean isValidToken(String token);

    /**
     * Сгенерировать токен и сохранить в базу
     *
     * @param userDetails  Информация о пользователе
     * @param user reference на пользователя
     * @return Сгенерированный токен
     */
    Token generateToken(UserDetails userDetails, User user);

    /**
     * Получить токен по refresh
     * @param refreshToken Действительный refresh токен
     *
     * @return Сущность токена либо null, если запись по refresh токену не найдена
     * (refresh токен истёк, токен был отозван или обновлён ранее)
     */
    Token getByRefreshToken(String refreshToken);

    /**
     * Ротация токена
     * @param token Обновляемый токен
     */
    Token refreshToken(Token token);

    /**
     * Возвращает строку jwt токена
     * @param id id токена
     */
    String getJwtById(UUID id);

    String getRefreshTokenById(UUID id);

    void deleteToken(UUID id);

    UUID getTokenId(String token);

    /**
     * Удалить все токены пользователя, кроме текущего
     */
    void deleteAllTokensExceptPresent(User user, String currentToken);

    void deleteExpiredTokens();

    /**
     * Извлекает ID пользователя из токена
     */
    UUID getUserId(String token);
}
