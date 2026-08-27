package ru.valkeru.libdemo.domain.repository.jpa.library_card;

import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import ru.valkeru.libdemo.domain.entity.LibraryCard;
import ru.valkeru.libdemo.domain.entity.user.User;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface LibraryCardRepository extends BaseJpaRepository<LibraryCard, UUID> {

    @Query("select exists(select 1 from LibraryCard rc where rc.user = :user and (rc.validTo is null or rc.validTo > :now))")
    boolean existsByUserAndNotExpired(User user, Instant now);

    @Query("""
        select exists(
            select 1 from LibraryCard rc
                where rc.user = :user
                and not rc.active
                and rc.inactivityReason = ru.valkeru.libdemo.domain.enums.LibraryCardInactivityReason.BANNED
            )
    """)
    boolean existsByUserAndBlocked(User user);

    Optional<LibraryCard> findByUserAndId(User user, UUID id);

    Page<LibraryCard> findAllByUser(User user, Pageable pageable);

    Optional<LibraryCard> findByUserAndActiveTrue(User user);

    @Modifying
    @Query("""
        update LibraryCard l
             set
                 l.active = false,
                 l.inactivityReason = ru.valkeru.libdemo.domain.enums.LibraryCardInactivityReason.EXPIRED
             where l.inactivityReason != ru.valkeru.libdemo.domain.enums.LibraryCardInactivityReason.BANNED
             and l.validTo < :threshold
    """)
    void inactivateExpiredBefore(Instant threshold);
}
