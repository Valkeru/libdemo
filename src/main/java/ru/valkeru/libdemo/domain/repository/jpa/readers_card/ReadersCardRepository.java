package ru.valkeru.libdemo.domain.repository.jpa.readers_card;

import io.hypersistence.utils.spring.repository.BaseJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import ru.valkeru.libdemo.domain.entity.ReadersCard;
import ru.valkeru.libdemo.domain.entity.user.User;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

public interface ReadersCardRepository extends BaseJpaRepository<ReadersCard, UUID> {

    @Query("select exists(select 1 from ReadersCard rc where rc.user = :user and (rc.validTo is null or rc.validTo > :now))")
    boolean existsByUserAndNotExpired(User user, Instant now);

    @Query("""
        select exists(
            select 1 from ReadersCard rc
                where rc.user = :user
                and not rc.active
                and rc.inactivityReason = ru.valkeru.libdemo.domain.enums.ReadersCardInactivityReason.BANNED
            )
    """)
    boolean existsByUserAndBlocked(User user);

    Optional<ReadersCard> findByUserAndId(User user, UUID id);

    Page<ReadersCard> findAllByUser(User user, Pageable pageable);

    ReadersCard findByUserAndActiveTrue(User user);
}
