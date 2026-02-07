package ru.valkeru.libdemo.repository.jpa.user;

import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.valkeru.libdemo.model.entity.user.Token;
import ru.valkeru.libdemo.model.entity.user.User;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface TokenRepository extends BaseJpaRepository<Token, UUID> {

    List<Token> getTokensByUserAndIdNot(User user, UUID id);

    @Query("select t.jwt from Token t where t.id = :id")
    String getJwtById(UUID id);

    @Query("select t.refreshToken from Token t where t.id = :id")
    String getRefreshTokenById(UUID id);

    /**
     * Получить ID токена, если не истёк refresh токен
     */
    @Query("select t from Token t where t.refreshToken = :refreshToken and t.refreshTokenExpiry > :now")
    Token getNotExpired(String refreshToken, Instant now);

    @Transactional
    @Modifying
    @Query("delete from Token t where t.refreshTokenExpiry <= :now")
    void deleteExpired(Instant now);

    @Transactional
    @Modifying
    @Query("""
        update Token t set t.jwt = :newJwt, t.refreshToken = :newRefresh,
             t.refreshTokenExpiry = :newRefreshTokenExpiry where t.id = :id
    """)
    void updateTokenById(UUID id, String newJwt, String newRefresh, Instant newRefreshTokenExpiry);
}
