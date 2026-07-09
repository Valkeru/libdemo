package ru.valkeru.libdemo.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.valkeru.libdemo.domain.entity.LibraryCard;
import ru.valkeru.libdemo.domain.entity.user.User;
import ru.valkeru.libdemo.domain.enums.LibraryCardCreateRestriction;
import ru.valkeru.libdemo.domain.enums.LibraryCardCreationContext;
import ru.valkeru.libdemo.domain.repository.jpa.library_card.LibraryCardRepository;
import ru.valkeru.libdemo.domain.service.LibraryCardService;
import ru.valkeru.libdemo.model.dto.LibraryCardCreateDto;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

@Service
@RequiredArgsConstructor
public class LibraryCardServiceImpl implements LibraryCardService {

    private final LibraryCardRepository repository;
    private final JdbcClient jdbcClient;

    @Override
    @Transactional(propagation = Propagation.MANDATORY)
    public LibraryCard createLibraryCard(User user, LibraryCardCreateDto dto) {
        LibraryCard card = LibraryCard.builder()
            .user(user)
            .validFrom(dto.getValidFrom())
            .validTo(dto.getValidTo())
            .number(getNextCardNumber())
            .build();

        return repository.persist(card);
    }

    @Override
    public LibraryCardCreateRestriction checkCreateRestrictions(User user, LibraryCardCreationContext context) {
        return switch (context) {
            case USER -> checkUserRestrictions(user);
            case STAFF -> checkStaffRestrictions(user);
        };
    }

    private LibraryCardCreateRestriction checkUserRestrictions(User user) {
        if (repository.existsByUserAndNotExpired(user, Instant.now())) {
            return LibraryCardCreateRestriction.CARD_EXISTS;
        }

        if (repository.existsByUserAndBlocked(user)) {
            return LibraryCardCreateRestriction.CARD_BLOCKED;
        }

        return LibraryCardCreateRestriction.NONE;
    }

    private LibraryCardCreateRestriction checkStaffRestrictions(User user) {
        if (repository.existsByUserAndNotExpired(user, Instant.now())) {
            return LibraryCardCreateRestriction.CARD_EXISTS;
        }

        return LibraryCardCreateRestriction.NONE;
    }

    /**
     * Returns a library card number as "year + in_year_sequental_number"<br/>
     * in_year_sequental_number is padded to 5 symbols. Samples:<br/>
     * <li>202600001</li>
     * <li>202600123</li>
     * <li>202612345</li>
     */
    private long getNextCardNumber() {
        long year = LocalDate.now(ZoneOffset.UTC).getYear();

        long value = jdbcClient.sql("""
            INSERT INTO counter.library_card_number (year, value) VALUES (:year, 1)
                ON CONFLICT (year) DO UPDATE SET value = value + 1
            RETURNING value
            """
        )
            .param("year", year)
            .query(Long.class)
            .single();

        return year * 100_000L + value;
    }
}
