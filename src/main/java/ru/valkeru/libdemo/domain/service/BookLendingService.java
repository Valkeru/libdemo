package ru.valkeru.libdemo.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.valkeru.libdemo.model.dto.internal.BookLendingCreateRequest;
import ru.valkeru.libdemo.model.dto.internal.BookLendingUpdateRequest;
import ru.valkeru.libdemo.domain.entity.BookLending;
import ru.valkeru.libdemo.domain.entity.user.User;
import ru.valkeru.libdemo.domain.enums.LendingStatus;
import ru.valkeru.libdemo.domain.projection.BookLendingListProjection;
import ru.valkeru.libdemo.domain.projection.BookLendingProjection;

import java.util.UUID;

public interface BookLendingService {

    BookLending createLending(BookLendingCreateRequest lendingCreateRequest);

    Page<BookLendingListProjection> getLendingListForUser(User user, Pageable pageable);

    void cancelReservation(UUID id);

    void cancelReservation(UUID id, User user);

    BookLendingProjection getLending(UUID id);

    BookLending getLending(UUID id, LendingStatus lendingStatus);

    void update(BookLendingUpdateRequest request, BookLending lending);

    BookLendingProjection getLendingForUser(UUID id, User user);

    void returnBook(BookLending lending);
}
