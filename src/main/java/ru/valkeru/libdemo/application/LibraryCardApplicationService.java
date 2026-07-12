package ru.valkeru.libdemo.application;

import org.springframework.data.domain.Page;
import ru.valkeru.libdemo.model.dto.LibraryCardCreateDto;
import ru.valkeru.libdemo.model.dto.LibraryCardDto;
import ru.valkeru.libdemo.model.dto.LibraryCardServiceDto;
import ru.valkeru.libdemo.model.dto.internal.LibraryCardCreateResult;
import ru.valkeru.libdemo.model.dto.security.LibraryPrincipal;

import java.util.UUID;

public interface LibraryCardApplicationService {

    LibraryCardServiceDto getCardForStaff(UUID id);

    LibraryCardDto getCardForUser(UUID id);

    Page<Object> getUserCards(LibraryPrincipal principal);

    /**
     * Get current not expired library card for principal
     */
    LibraryCardDto getCurrentCard(LibraryPrincipal principal);

    /**
     * Service method to create a library card for user
     */
    UUID createLibraryCard(LibraryCardCreateDto dto);

    /**
     * User method to create a library card for themselves<br/>
     * If created by user, library card is valid for 1 year
     */
    LibraryCardCreateResult createLibraryCard(LibraryPrincipal principal);
}
