package ru.valkeru.libdemo.persistence.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.valkeru.libdemo.persistence.entity.ReadersCard;
import ru.valkeru.libdemo.persistence.entity.user.User;
import ru.valkeru.libdemo.persistence.enums.ReadersCardCreateRestriction;
import ru.valkeru.libdemo.persistence.enums.ReadersCardCreationContext;
import ru.valkeru.libdemo.persistence.exception.ConflictPersistenceException;
import ru.valkeru.libdemo.persistence.exception.NotFoundPersistenceException;
import ru.valkeru.libdemo.persistence.repository.jpa.readers_card.ReadersCardRepository;
import ru.valkeru.libdemo.persistence.service.ReadersCardService;
import ru.valkeru.libdemo.model.dto.ReadersCardCreateDto;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReadersCardServiceImpl implements ReadersCardService {

    private final ReadersCardRepository repository;
    private final JdbcClient jdbcClient;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public ReadersCard createReadersCard(User user, ReadersCardCreateDto dto) {
        ReadersCard card = ReadersCard.builder()
            .user(user)
            .validFrom(dto.getValidFrom())
            .validTo(dto.getValidTo())
            .number(getNextCardNumber())
            .build();

        return repository.persist(card);
    }

    @Override
    public ReadersCardCreateRestriction checkCreateRestrictions(User user, ReadersCardCreationContext context) {
        return switch (context) {
            case USER -> checkUserRestrictions(user);
            case STAFF -> checkStaffRestrictions(user);
        };
    }

    private ReadersCardCreateRestriction checkUserRestrictions(User user) {
        if (repository.existsByUserAndNotExpired(user, Instant.now())) {
            return ReadersCardCreateRestriction.CARD_EXISTS;
        }

        if (repository.existsByUserAndBlocked(user)) {
            return ReadersCardCreateRestriction.CARD_BLOCKED;
        }

        return ReadersCardCreateRestriction.NONE;
    }

    @Override
    public Page<ReadersCard> getReadersCards(User user, Pageable pageable) {
        return repository.findAllByUser(user, pageable);
    }

    @Override
    public ReadersCard getReadersCard(User user, UUID id) {
        return repository.findByUserAndId(user, id)
            .orElseThrow(NotFoundPersistenceException::readersCard);
    }

    @Override
    public ReadersCard findCurrentReadersCard(User user) {
        return repository.findByUserAndActiveTrue(user)
            .orElse(null);
    }

    @Override
    public ReadersCard requireCurrentReadersCard(User user) {
        return repository.findByUserAndActiveTrue(user)
            .orElseThrow(ConflictPersistenceException::hasNoReadersCard);
    }

    @Override
    public ReadersCard getActiveReadersCard(UUID readersCardId) {
        ReadersCard readersCard = repository.findById(readersCardId)
            .orElseThrow(NotFoundPersistenceException::readersCard);

        if (!readersCard.isActive()) {
            throw ConflictPersistenceException.notActiveCard();
        }

        return readersCard;
    }

    private ReadersCardCreateRestriction checkStaffRestrictions(User user) {
        if (repository.existsByUserAndNotExpired(user, Instant.now())) {
            return ReadersCardCreateRestriction.CARD_EXISTS;
        }

        return ReadersCardCreateRestriction.NONE;
    }

    /**
     * Returns a readers card number as "year + in_year_sequental_number"<br/>
     * in_year_sequental_number is padded to 5 symbols. Samples:<br/>
     * <li>202600001</li>
     * <li>202600123</li>
     * <li>202612345</li>
     */
    private synchronized long getNextCardNumber() {
        long year = LocalDate.now(ZoneOffset.UTC).getYear();

        long value = jdbcClient.sql("""
            INSERT INTO counter.readers_card_number (year, value) VALUES (:year, 1)
                ON CONFLICT (year) DO UPDATE SET value = counter.readers_card_number.value + 1
            RETURNING value
            """
        )
            .param("year", year)
            .query(Long.class)
            .single();

        return year * 100_000L + value;
    }
}
