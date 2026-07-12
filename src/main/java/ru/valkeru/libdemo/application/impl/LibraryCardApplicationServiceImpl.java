package ru.valkeru.libdemo.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.valkeru.libdemo.domain.entity.user.User;
import ru.valkeru.libdemo.domain.enums.LibraryCardCreateRestriction;
import ru.valkeru.libdemo.domain.enums.LibraryCardCreationContext;
import ru.valkeru.libdemo.domain.service.LibraryCardService;
import ru.valkeru.libdemo.exception.impl.LibraryCardRestrictedException;
import ru.valkeru.libdemo.model.dto.LibraryCardCreateDto;
import ru.valkeru.libdemo.model.dto.LibraryCardDto;
import ru.valkeru.libdemo.model.dto.LibraryCardServiceDto;
import ru.valkeru.libdemo.model.dto.internal.LibraryCardCreateResult;
import ru.valkeru.libdemo.model.dto.security.LibraryPrincipal;
import ru.valkeru.libdemo.security.Role;
import ru.valkeru.libdemo.application.LibraryCardApplicationService;
import ru.valkeru.libdemo.infrastructure.security.UserService;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class LibraryCardApplicationServiceImpl implements LibraryCardApplicationService {

    private final LibraryCardService libraryCardService;
    private final UserService userService;

    @Override
    public LibraryCardServiceDto getCardForStaff(UUID id) {
        return null;
    }

    @Override
    public LibraryCardDto getCardForUser(UUID id) {
        return null;
    }

    @Override
    public Page<Object> getUserCards(LibraryPrincipal principal) {
        return null;
    }

    @Override
    public LibraryCardDto getCurrentCard(LibraryPrincipal principal) {
        return null;
    }

    @Override
    @Transactional
    public UUID createLibraryCard(LibraryCardCreateDto dto) {
        User user = userService.getReference(dto.getUserId());

        LibraryCardCreateRestriction restriction = libraryCardService.checkCreateRestrictions(user, LibraryCardCreationContext.STAFF);

        return switch (restriction) {
            case CARD_EXISTS -> throw LibraryCardRestrictedException.exists();
            case NONE -> libraryCardService.createLibraryCard(user, dto).getId();
            default -> throw new IllegalStateException("Unexpected restriction for STAFF: %s".formatted(restriction));
        };
    }

    @Override
    @Transactional
    public LibraryCardCreateResult createLibraryCard(LibraryPrincipal principal) {
        User user = userService.getReference(principal.id());

        Instant now = Instant.now();
        Instant validTo = Instant.from(
            LocalDate.ofInstant(now, ZoneOffset.UTC).plusYears(1).atStartOfDay()
        );

        LibraryCardCreateDto createDto = LibraryCardCreateDto.builder()
            .validFrom(now)
            .validTo(validTo)
            .build();

        LibraryCardCreateRestriction restriction = libraryCardService.checkCreateRestrictions(user, LibraryCardCreationContext.USER);

        UUID cardId = switch (restriction) {
            case CARD_EXISTS -> throw LibraryCardRestrictedException.exists();
            case CARD_BLOCKED -> throw LibraryCardRestrictedException.blocked();
            case NONE -> libraryCardService.createLibraryCard(user, createDto).getId();
        };

        if (principal.role() == Role.ROLE_USER) {
            // TODO: Update role and tokens
        }

        return new LibraryCardCreateResult(cardId, null);
    }
}
