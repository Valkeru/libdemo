package ru.valkeru.libdemo.repository.jpa.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.valkeru.libdemo.model.entity.user.Token;
import ru.valkeru.libdemo.model.entity.user.User;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface TokenRepository extends JpaRepository<Token, UUID> {

    List<Token> getTokensByUserAndIdNot(User user, UUID id);

    @Query("select t.jwt from Token t where t.id = :id")
    String getJwtById(UUID id);

    @Query("select t.refreshToken from Token t where t.id = :id")
    String getRefreshTokenById(UUID id);

    /**
     * Получить ID токена, если не истёк refresh токен
     */
    @Query("select t.id from Token t where t.refreshToken = :refreshToken and t.refreshTokenExpiry > CURRENT_TIMESTAMP")
    UUID getNotExpiredTokenId(String refreshToken);

    @Transactional
    @Modifying
    @Query("""
        update Token t set t.jwt = :newJwt, t.refreshToken = :newRefresh,
             t.refreshTokenExpiry = :newRefreshTokenExpiry where t.id = :id
    """)
    void updateTokenById(UUID id, String newJwt, String newRefresh, Instant newRefreshTokenExpiry);
}
