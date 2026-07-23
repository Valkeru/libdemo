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
import ru.valkeru.libdemo.persistence.entity.ReadersCard;
import ru.valkeru.libdemo.persistence.entity.user.Token;
import ru.valkeru.libdemo.persistence.entity.user.User;
import ru.valkeru.libdemo.persistence.enums.ReadersCardCreateRestriction;
import ru.valkeru.libdemo.persistence.enums.ReadersCardCreationContext;
import ru.valkeru.libdemo.persistence.service.ReadersCardService;
import ru.valkeru.libdemo.exception.impl.ReadersCardRestrictedException;
import ru.valkeru.libdemo.infrastructure.security.JWTService;
import ru.valkeru.libdemo.mapper.ReadersCardMapper;
import ru.valkeru.libdemo.model.dto.ReadersCardCreateDto;
import ru.valkeru.libdemo.model.dto.ReadersCardDto;
import ru.valkeru.libdemo.model.dto.ReadersCardListDto;
import ru.valkeru.libdemo.model.dto.ReadersCardServiceDto;
import ru.valkeru.libdemo.model.dto.internal.ReadersCardCreateResult;
import ru.valkeru.libdemo.model.dto.internal.TokenDto;
import ru.valkeru.libdemo.model.dto.security.LibraryPrincipal;
import ru.valkeru.libdemo.security.Role;
import ru.valkeru.libdemo.application.ReadersCardApplicationService;
import ru.valkeru.libdemo.infrastructure.security.UserService;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.EnumSet;
import java.util.Set;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ReadersCardApplicationServiceImpl implements ReadersCardApplicationService {

    private static final Set<Role> ALLOWED_USER_ROLES = EnumSet.of(Role.ROLE_USER, Role.ROLE_READER);

    private final ReadersCardService readersCardService;
    private final UserService userService;
    private final JWTService jWTService;
    private final ReadersCardMapper readersCardMapper;

    @Override
    public ReadersCardServiceDto getReadersCard(UUID id) {
        return null;
    }

    @Override
    public ReadersCardDto getReadersCard(LibraryPrincipal principal, UUID id) {
        User user = userService.getReference(principal.id());

        ReadersCard readersCard = readersCardService.getReadersCard(user, id);

        return readersCardMapper.toDto(readersCard);
    }

    @Override
    public Page<ReadersCardListDto> getUserCards(LibraryPrincipal principal, Pageable pageable) {
        User user = userService.getReference(principal.id());
        Page<ReadersCard> cards = readersCardService.getReadersCards(user, pageable);

        return cards.map(readersCardMapper::toListDto);
    }

    @Override
    public ReadersCardDto getCurrentCard(LibraryPrincipal principal) {
        User user = userService.getReference(principal.id());
        ReadersCard readersCard = readersCardService.findCurrentReadersCard(user);

        return readersCardMapper.toDto(readersCard);
    }

    @Override
    @Transactional
    public UUID createReadersCard(ReadersCardCreateDto dto) {
        User user = userService.getById(dto.getUserId());

        if (!ALLOWED_USER_ROLES.contains(user.getRole())) {
            throwReadersCardCreationIsForbidden();
        }

        ReadersCardCreateRestriction restriction = readersCardService.checkCreateRestrictions(user, ReadersCardCreationContext.STAFF);

        return switch (restriction) {
            case CARD_EXISTS -> throw ReadersCardRestrictedException.exists();
            case NONE -> createReadersCardAndPromoteToReader(dto, user);
            default -> throw new IllegalStateException("Unexpected restriction for STAFF: %s".formatted(restriction));
        };
    }

    @Override
    @Transactional
    public ReadersCardCreateResult createReadersCard(LibraryPrincipal principal) {
        if (!ALLOWED_USER_ROLES.contains(principal.role())) {
            throwReadersCardCreationIsForbidden();
        }

        User user = userService.getReference(principal.id());

        Instant now = Instant.now();
        Instant validTo = LocalDate.now()
            .plusYears(1)
            .plusDays(1)
            .atStartOfDay()
            .toInstant(ZoneOffset.UTC);

        ReadersCardCreateDto createDto = ReadersCardCreateDto.builder()
            .validFrom(now)
            .validTo(validTo)
            .build();

        ReadersCardCreateRestriction restriction = readersCardService.checkCreateRestrictions(user, ReadersCardCreationContext.USER);

        UUID cardId = switch (restriction) {
            case CARD_EXISTS -> throw ReadersCardRestrictedException.exists();
            case CARD_BLOCKED -> throw ReadersCardRestrictedException.blocked();
            case NONE -> readersCardService.createReadersCard(user, createDto).getId();
        };

        return new ReadersCardCreateResult(cardId, promoteUserToReaderAndReissueJWT(principal, user));
    }

    private static void throwReadersCardCreationIsForbidden() {
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "The specified user is not allowed to have a readers card");
    }

    /**
     * Updates user's role if needed. If role was changed, new JWT will be issued
     *
     * @param principal Authenticated principal
     * @param user Authenticated user
     * @return Token if role was changed or null otherwise
     */
    private @Nullable TokenDto promoteUserToReaderAndReissueJWT(LibraryPrincipal principal, User user) {
        if (Role.ROLE_USER != principal.role()) {
            return null;
        }

        userService.updateRole(user, Role.ROLE_READER);
        UserDetails userDetails = userService.loadUserByUsername(user.getUsername());

        Token token = jWTService.generateToken(userDetails, user);

        return TokenDto.builder()
            .accessToken(token.getJwt())
            .refreshToken(token.getRefreshToken())
            .build();
    }

    /**
     * Creates a readers card for user and updates user's role if needed
     */
    private UUID createReadersCardAndPromoteToReader(ReadersCardCreateDto dto, User user) {
        UUID cardId = readersCardService.createReadersCard(user, dto).getId();
        if (user.getRole() == Role.ROLE_USER) {
            userService.updateRole(user, Role.ROLE_READER);
        }

        return cardId;
    }
}
