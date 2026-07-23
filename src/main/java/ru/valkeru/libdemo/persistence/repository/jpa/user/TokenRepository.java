package ru.valkeru.libdemo.persistence.repository.jpa.user;

import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.valkeru.libdemo.persistence.entity.user.Token;
import ru.valkeru.libdemo.persistence.entity.user.User;

import java.time.Instant;
import java.util.UUID;

public interface TokenRepository extends BaseJpaRepository<Token, UUID> {

    @Modifying
    void deleteByUserAndIdNot(User user, UUID id);

    @Query("select t.jwt from Token t where t.id = :id")
    String getJwtById(UUID id);

    @Query("select t.refreshToken from Token t where t.id = :id")
    String getRefreshTokenById(UUID id);

    /**
     * Get token ID if refresh token is not expired
     */
    @Query("select t from Token t where t.refreshToken = :refreshToken and t.refreshTokenExpiry > :now")
    Token getNotExpired(String refreshToken, Instant now);

    @Modifying
    @Query("delete from Token t where t.refreshTokenExpiry <= :now")
    void deleteExpired(Instant now);
}
