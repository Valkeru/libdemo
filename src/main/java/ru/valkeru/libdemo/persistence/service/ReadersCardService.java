package ru.valkeru.libdemo.persistence.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.valkeru.libdemo.persistence.entity.ReadersCard;
import ru.valkeru.libdemo.persistence.entity.user.User;
import ru.valkeru.libdemo.persistence.enums.ReadersCardCreateRestriction;
import ru.valkeru.libdemo.persistence.enums.ReadersCardCreationContext;
import ru.valkeru.libdemo.model.dto.ReadersCardCreateDto;

import java.util.UUID;

public interface ReadersCardService {

    /**
     * Creates readers card for user. Transactional context is required
     */
    ReadersCard createReadersCard(User user, ReadersCardCreateDto dto);

    /**
     * Check if user is allowed to create a readers card
     *
     * @return Check result<br/>
     * User is restricted to create the readers card if:<br/>
     * {@link  ReadersCardCreateRestriction#CARD_EXISTS} - readers card exists and not restricted (not expired or blocked)<br/>
     * {@link ReadersCardCreateRestriction#CARD_BLOCKED} - latest readers card is blocked.
     * Only staff can create a new card if latest card is expired
     */
    ReadersCardCreateRestriction checkCreateRestrictions(User user, ReadersCardCreationContext context);

    Page<ReadersCard> getReadersCards(User user, Pageable pageable);

    ReadersCard getReadersCard(User user, UUID id);

    ReadersCard findCurrentReadersCard(User user);

    ReadersCard requireCurrentReadersCard(User user);

    ReadersCard getActiveReadersCard(UUID readersCardId);
}
