package ru.valkeru.libdemo.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.valkeru.libdemo.domain.entity.LibraryCard;
import ru.valkeru.libdemo.domain.entity.user.User;
import ru.valkeru.libdemo.domain.enums.LibraryCardCreateRestriction;
import ru.valkeru.libdemo.domain.enums.LibraryCardCreationContext;
import ru.valkeru.libdemo.model.dto.LibraryCardCreateDto;

import java.time.Instant;
import java.util.UUID;

public interface LibraryCardService {

    /**
     * Creates library card for user. Transactional context is required
     */
    LibraryCard createLibraryCard(User user, LibraryCardCreateDto dto);

    /**
     * Check if user is allowed to create a library card
     *
     * @return Check result<br/>
     * User is restricted to create the library card if:<br/>
     * {@link  LibraryCardCreateRestriction#CARD_EXISTS} - library card exists and not restricted (not expired or blocked)<br/>
     * {@link LibraryCardCreateRestriction#CARD_BLOCKED} - latest library card is blocked.
     * Only staff can create a new card if latest card is expired
     */
    LibraryCardCreateRestriction checkCreateRestrictions(User user, LibraryCardCreationContext context);

    Page<LibraryCard> getLibraryCards(User user, Pageable pageable);

    LibraryCard getLibraryCard(User user, UUID id);

    LibraryCard findCurrentLibraryCard(User user);

    LibraryCard requireCurrentLibraryCard(User user);

    LibraryCard getActiveLibraryCard(UUID libraryCardId);

    /**
     * Inactivates library cards expired before the specified time
     */
    void inactivateExpiredBefore(Instant threshold);
}
