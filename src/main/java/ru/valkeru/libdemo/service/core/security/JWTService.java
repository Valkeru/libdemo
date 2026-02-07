package ru.valkeru.libdemo.service.core.security;

import jakarta.persistence.Tuple;
import org.springframework.security.core.userdetails.UserDetails;
import ru.valkeru.libdemo.model.entity.user.Token;
import ru.valkeru.libdemo.model.entity.user.User;

import java.util.List;
import java.util.UUID;

/**
 * Сервис реализует логику работы с jwt
 */
public interface JWTService {

    /**
     * Возвращает данные о субъекте и его роли, упакованные в кортеж
     * Ключи кортежа - subject и role
     */
    Tuple getPayload(String token);

    boolean isValidToken(String token);

    /**
     * Сгенерировать токен и сохранить в базу
     * @param user Пользователь
     *
     * @return ID сгенерированного токена
     */
    Token generateToken(UserDetails user);

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
     * Получить все токены пользователя, кроме текущего
     */
    List<Token> getAllTokensExceptPresent(User user, UUID tokenId);

    void deleteExpiredTokens();

    /**
     * Извлекает имя пользователя из токена
     */
    String getUserName(String token);
}
