package ru.valkeru.libdemo.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.valkeru.libdemo.model.dto.ReadersCardCreateDto;
import ru.valkeru.libdemo.model.dto.ReadersCardDto;
import ru.valkeru.libdemo.model.dto.ReadersCardListDto;
import ru.valkeru.libdemo.model.dto.ReadersCardServiceDto;
import ru.valkeru.libdemo.model.dto.internal.ReadersCardCreateResult;
import ru.valkeru.libdemo.model.dto.security.LibraryPrincipal;

import java.util.UUID;

public interface ReadersCardApplicationService {

    /**
     * Gets readers card by the specified ID
     * @param id Card ID
     */
    ReadersCardServiceDto getReadersCard(UUID id);

    /**
     * Gets the authenticated user's card by the specified card ID
     * @param principal Current principal
     * @param id Card ID
     */
    ReadersCardDto getReadersCard(LibraryPrincipal principal, UUID id);

    /**
     * Gets the authenticated user's readers cards, including current and expired cards
     * @param principal Current principal
     */
    Page<ReadersCardListDto> getUserCards(LibraryPrincipal principal, Pageable pageable);

    /**
     * Gets the current valid readers card for the authenticated user
     */
    ReadersCardDto getCurrentCard(LibraryPrincipal principal);

    /**
     * Creates a readers card for a user by staff request.<br/>
     * The user is expected to have {@code ROLE_USER} or {@code ROLE_READER}.
     * If the user has {@code ROLE_USER}, their role will be changed to {@code ROLE_READER}
     */
    UUID createReadersCard(ReadersCardCreateDto dto);

    /**
     * Creates a readers card for authenticated user.<br/>
     * Readers cards created by users are valid for one year.<br/>
     * The authenticated user is expected to have either
     * {@code ROLE_USER} or {@code ROLE_READER}. If the authenticated user has {@code ROLE_USER},
     * their role will be changed to {@code ROLE_READER} and a new JWT will be issued
     */
    ReadersCardCreateResult createReadersCard(LibraryPrincipal principal);
}
