package ru.valkeru.libdemo.application.impl;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.valkeru.libdemo.domain.entity.LibraryCard;
import ru.valkeru.libdemo.domain.entity.user.Token;
import ru.valkeru.libdemo.domain.entity.user.User;
import ru.valkeru.libdemo.domain.enums.LibraryCardCreateRestriction;
import ru.valkeru.libdemo.domain.enums.LibraryCardCreationContext;
import ru.valkeru.libdemo.domain.service.LibraryCardService;
import ru.valkeru.libdemo.exception.impl.LibraryCardRestrictedException;
import ru.valkeru.libdemo.infrastructure.security.JWTService;
import ru.valkeru.libdemo.mapper.LibraryCardMapper;
import ru.valkeru.libdemo.model.dto.LibraryCardCreateDto;
import ru.valkeru.libdemo.model.dto.LibraryCardDto;
import ru.valkeru.libdemo.model.dto.LibraryCardListDto;
import ru.valkeru.libdemo.model.dto.LibraryCardServiceDto;
import ru.valkeru.libdemo.model.dto.internal.LibraryCardCreateResult;
import ru.valkeru.libdemo.model.dto.internal.TokenDto;
import ru.valkeru.libdemo.model.dto.security.LibraryPrincipal;
import ru.valkeru.libdemo.security.Role;
import ru.valkeru.libdemo.application.LibraryCardApplicationService;
import ru.valkeru.libdemo.infrastructure.security.UserService;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LibraryCardApplicationServiceImpl implements LibraryCardApplicationService {

    private static final Set<Role> ALLOWED_USER_ROLES = EnumSet.of(Role.USER, Role.READER);

    private final LibraryCardService libraryCardService;
    private final UserService userService;
    private final JWTService jWTService;
    private final LibraryCardMapper libraryCardMapper;

    @Override
    public LibraryCardServiceDto getLibraryCard(UUID id) {
        return null;
    }

    @Override
    public LibraryCardDto getLibraryCard(LibraryPrincipal principal, UUID id) {
        User user = userService.getReference(principal.id());

        LibraryCard libraryCard = libraryCardService.getLibraryCard(user, id);

        return libraryCardMapper.toDto(libraryCard);
    }

    @Override
    public Page<LibraryCardListDto> getUserCards(LibraryPrincipal principal, Pageable pageable) {
        User user = userService.getReference(principal.id());
        Page<LibraryCard> cards = libraryCardService.getLibraryCards(user, pageable);

        return cards.map(libraryCardMapper::toListDto);
    }

    @Override
    public LibraryCardDto getCurrentCard(LibraryPrincipal principal) {
        User user = userService.getReference(principal.id());
        LibraryCard libraryCard = libraryCardService.findCurrentLibraryCard(user);

        return libraryCardMapper.toDto(libraryCard);
    }

    @Override
    @Transactional
    public UUID createLibraryCard(LibraryCardCreateDto dto) {
        User user = userService.getById(dto.getUserId());

        if (!ALLOWED_USER_ROLES.contains(user.getRole())) {
            throwLibraryCardCreationIsForbidden();
        }

        LibraryCardCreateRestriction restriction = libraryCardService.checkCreateRestrictions(user, LibraryCardCreationContext.STAFF);

        return switch (restriction) {
            case CARD_EXISTS -> throw LibraryCardRestrictedException.exists();
            case NONE -> createLibraryCardAndPromoteToReader(dto, user);
            default -> throw new IllegalStateException("Unexpected restriction for STAFF: %s".formatted(restriction));
        };
    }

    @Override
    @Transactional
    public LibraryCardCreateResult createLibraryCard(LibraryPrincipal principal) {
        if (!ALLOWED_USER_ROLES.contains(principal.role())) {
            throwLibraryCardCreationIsForbidden();
        }

        User user = userService.getReference(principal.id());

        Instant now = Instant.now();
        Instant validTo = LocalDate.now()
            .plusYears(1)
            .plusDays(1)
            .atStartOfDay()
            .toInstant(ZoneOffset.UTC);

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

        return new LibraryCardCreateResult(cardId, promoteUserToReaderAndReissueJWT(principal, user));
    }

    @Override
    public void inactivateExpiredLibraryCards() {
        Instant now = Instant.now();
        libraryCardService.inactivateExpiredBefore(now);
        Collection<User> readersWithoutLibraryCards = userService.getReadersWithoutLibraryCards();
        for (User reader : readersWithoutLibraryCards) {
            demoteReaderToUserAndReissueJWT(reader);
        }
    }

    private static void throwLibraryCardCreationIsForbidden() {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The specified user is not allowed to have a library card");
    }

    /**
     * Updates user's role if needed. If role was changed, new JWT will be issued
     *
     * @param principal Authenticated principal
     * @param user Authenticated user
     * @return Token if role was changed or null otherwise
     */
    private @Nullable TokenDto promoteUserToReaderAndReissueJWT(LibraryPrincipal principal, User user) {
        if (Role.USER != principal.role()) {
            return null;
        }

        userService.updateRole(user, Role.READER);

        Token token = jWTService.generateToken(user);

        return TokenDto.builder()
            .accessToken(token.getJwt())
            .refreshToken(token.getRefreshToken())
            .build();
    }

    /**
     * Resets user's READER role to USER role and reissues JWT token with new authorities
     */
    private void demoteReaderToUserAndReissueJWT(User user) {
        if (Role.READER != user.getRole()) {
            return;
        }

        userService.updateRole(user, Role.READER);

        jWTService.generateToken(user);
    }

    /**
     * Creates a library card for user and updates user's role if needed
     */
    private UUID createLibraryCardAndPromoteToReader(LibraryCardCreateDto dto, User user) {
        UUID cardId = libraryCardService.createLibraryCard(user, dto).getId();
        if (user.getRole() == Role.USER) {
            userService.updateRole(user, Role.READER);
        }

        return cardId;
    }
}
