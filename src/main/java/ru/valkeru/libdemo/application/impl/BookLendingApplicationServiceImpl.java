package ru.valkeru.libdemo.application.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.valkeru.libdemo.application.BookLendingApplicationService;
import ru.valkeru.libdemo.model.dto.internal.BookLendingCreateRequest;
import ru.valkeru.libdemo.model.dto.internal.BookLendingUpdateRequest;
import ru.valkeru.libdemo.persistence.entity.Book;
import ru.valkeru.libdemo.persistence.entity.BookLending;
import ru.valkeru.libdemo.persistence.entity.BookInstance;
import ru.valkeru.libdemo.persistence.entity.ReadersCard;
import ru.valkeru.libdemo.persistence.entity.user.User;
import ru.valkeru.libdemo.persistence.enums.LendingStatus;
import ru.valkeru.libdemo.persistence.projection.BookLendingListProjection;
import ru.valkeru.libdemo.persistence.projection.BookLendingProjection;
import ru.valkeru.libdemo.persistence.service.BookLendingService;
import ru.valkeru.libdemo.persistence.service.BookInstanceService;
import ru.valkeru.libdemo.persistence.service.BookService;
import ru.valkeru.libdemo.persistence.service.ReadersCardService;
import ru.valkeru.libdemo.infrastructure.security.UserService;
import ru.valkeru.libdemo.mapper.BookLendingMapper;
import ru.valkeru.libdemo.model.dto.lending.BookLendingDto;
import ru.valkeru.libdemo.model.dto.lending.BookLendingListDto;
import ru.valkeru.libdemo.model.dto.security.LibraryPrincipal;

import java.time.Instant;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneOffset;
import java.util.UUID;

@Component
@Transactional(readOnly = true)
public class BookLendingApplicationServiceImpl implements BookLendingApplicationService {

    private final UserService userService;
    private final BookService bookService;
    private final BookInstanceService bookInstanceService;
    private final ReadersCardService readersCardService;
    private final BookLendingService service;
    private final BookLendingMapper mapper;
    private final Period reservationPeriod;
    private final Period lendingPeriod;

    public BookLendingApplicationServiceImpl(UserService userService, BookService bookService,
                                             BookInstanceService bookInstanceService,
                                             ReadersCardService readersCardService, BookLendingService service,
                                             BookLendingMapper mapper,
                                             @Value("P${app.policy.reservation-period-days}D") Period reservationPeriod,
                                             @Value("P${app.policy.lending-period-days}D") Period lendingPeriod) {
        this.userService = userService;
        this.bookService = bookService;
        this.bookInstanceService = bookInstanceService;
        this.readersCardService = readersCardService;
        this.service = service;
        this.mapper = mapper;
        this.reservationPeriod = reservationPeriod;
        this.lendingPeriod = lendingPeriod;
    }

    @Override
    public UUID createLending(UUID bookId, UUID readersCardId) {
        ReadersCard readersCard = readersCardService.getActiveReadersCard(readersCardId);
        BookLending lending = createReservedLending(bookId, readersCard);

        return lending.getId();
    }

    @Override
    @Transactional
    public UUID createLending(UUID bookId, LibraryPrincipal principal) {
        User user = userService.getReference(principal.id());

        ReadersCard readersCard = readersCardService.requireCurrentReadersCard(user);
        BookLending lending = createReservedLending(bookId, readersCard);

        return lending.getId();
    }

    @Override
    public BookLendingDto getLending(UUID id) {
        BookLendingProjection lending = service.getLending(id);

        return mapper.toDto(lending);
    }

    @Override
    public BookLendingDto getLending(UUID id, LibraryPrincipal principal) {
        User user = userService.getReference(principal.id());
        BookLendingProjection lending = service.getLendingForUser(id, user);

        return mapper.toDto(lending);
    }

    @Override
    @Transactional
    public void issueReservedBook(UUID reservedLendingId) {
        BookLending lending = service.getLending(reservedLendingId, LendingStatus.RESERVED);

        Instant borrowedAt = Instant.now();
        LocalDate returnDueDate = borrowedAt.atZone(ZoneOffset.UTC)
            .toLocalDate()
            .plus(lendingPeriod);

        BookLendingUpdateRequest request = BookLendingUpdateRequest.builder()
            .borrowedAt(borrowedAt)
            .returnDueDate(returnDueDate)
            .status(LendingStatus.BORROWED)
            .build();

        service.update(request, lending);
    }

    @Override
    public void cancelLending(UUID id) {
        service.cancelReservation(id);
    }

    @Override
    @Transactional
    public void cancelLending(UUID id, LibraryPrincipal principal) {
        User user = userService.getReference(principal.id());
        service.cancelReservation(id, user);
    }

    @Override
    public Page<BookLendingListDto> listLendings(LibraryPrincipal principal, Pageable pageable) {
        User user = userService.getReference(principal.id());
        Page<BookLendingListProjection> projectionPage = service.getLendingListForUser(user, pageable);

        return projectionPage.map(mapper::toListDto);
    }

    @Override
    @Transactional
    public void returnBook(UUID lendingId) {
        BookLending lending = service.getLending(lendingId, LendingStatus.BORROWED);
        service.returnBook(lending);
    }

    private BookLending createReservedLending(UUID bookId, ReadersCard readersCard) {
        Book book = bookService.getBookById(bookId);
        BookInstance bookInstance = bookInstanceService.getAvailableInstance(book);

        Instant reservedAt = Instant.now();
        LocalDate reserveDueDate = reservedAt.atZone(ZoneOffset.UTC)
            .toLocalDate()
            .plus(reservationPeriod);

        BookLendingCreateRequest lendingCreateRequest = BookLendingCreateRequest.builder()
            .bookInstance(bookInstance)
            .readersCard(readersCard)
            .reservedAt(reservedAt)
            .reservationDueDate(reserveDueDate)
            .status(LendingStatus.RESERVED)
            .build();

        return service.createLending(lendingCreateRequest);
    }
}
