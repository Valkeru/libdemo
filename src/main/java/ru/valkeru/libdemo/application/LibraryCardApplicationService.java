package ru.valkeru.libdemo.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.valkeru.libdemo.model.dto.LibraryCardCreateDto;
import ru.valkeru.libdemo.model.dto.LibraryCardDto;
import ru.valkeru.libdemo.model.dto.LibraryCardListDto;
import ru.valkeru.libdemo.model.dto.LibraryCardServiceDto;
import ru.valkeru.libdemo.model.dto.internal.LibraryCardCreateResult;
import ru.valkeru.libdemo.model.dto.security.LibraryPrincipal;

import java.util.UUID;

public interface LibraryCardApplicationService {

    /**
     * Gets library card by the specified ID
     * @param id Card ID
     */
    LibraryCardServiceDto getLibraryCard(UUID id);

    /**
     * Gets the authenticated user's card by the specified card ID
     * @param principal Current principal
     * @param id Card ID
     */
    LibraryCardDto getLibraryCard(LibraryPrincipal principal, UUID id);

    /**
     * Gets the authenticated user's library cards, including current and expired cards
     * @param principal Current principal
     */
    Page<LibraryCardListDto> getUserCards(LibraryPrincipal principal, Pageable pageable);

    /**
     * Gets the current valid library card for the authenticated user
     */
    LibraryCardDto getCurrentCard(LibraryPrincipal principal);

    /**
     * Creates a library card for a user by staff request.<br/>
     * The user is expected to have {@code USER} or {@code READER}.
     * If the user has {@code USER}, their role will be changed to {@code READER}
     */
    UUID createLibraryCard(LibraryCardCreateDto dto);

    /**
     * Creates a library card for authenticated user.<br/>
     * Library cards created by users are valid for one year.<br/>
     * The authenticated user is expected to have either
     * {@code USER} or {@code READER}. If the authenticated user has {@code USER},
     * their role will be changed to {@code READER} and a new JWT will be issued
     */
    LibraryCardCreateResult createLibraryCard(LibraryPrincipal principal);

    /**
     * Inactivates expired library cards. Role will be changed to {@code USER} for users having no any active cards
     * except users whose cards are blocked
     */
    void inactivateExpiredLibraryCards();
}
