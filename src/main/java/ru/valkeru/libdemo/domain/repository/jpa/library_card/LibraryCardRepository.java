package ru.valkeru.libdemo.domain.repository.jpa.library_card;

import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.valkeru.libdemo.domain.entity.LibraryCard;
import ru.valkeru.libdemo.domain.entity.user.User;

import java.time.Instant;
import java.util.UUID;

public interface LibraryCardRepository extends BaseJpaRepository<LibraryCard, UUID> {

    @Query("select exists(select 1 from LibraryCard l where l.user = :user and (l.validTo is null or l.validTo > :now))")
    boolean existsByUserAndNotExpired(User user, Instant now);

    @Query("""
        select exists(
            select 1 from LibraryCard l
                where l.user = :user
                and not l.active
                and l.inactivityReason = ru.valkeru.libdemo.domain.enums.InactivityReason.BANNED
            )
    """)
    boolean existsByUserAndBlocked(User user);
}
