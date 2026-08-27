package ru.valkeru.libdemo.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.valkeru.libdemo.mapper.BookLendingMapper;
import ru.valkeru.libdemo.model.dto.internal.BookLendingCreateRequest;
import ru.valkeru.libdemo.model.dto.internal.BookLendingUpdateRequest;
import ru.valkeru.libdemo.domain.entity.BookLending;
import ru.valkeru.libdemo.domain.entity.user.User;
import ru.valkeru.libdemo.domain.enums.LendingStatus;
import ru.valkeru.libdemo.domain.exception.ConflictDomainException;
import ru.valkeru.libdemo.domain.exception.NotFoundDomainException;
import ru.valkeru.libdemo.domain.projection.BookLendingListProjection;
import ru.valkeru.libdemo.domain.projection.BookLendingProjection;
import ru.valkeru.libdemo.domain.repository.jpa.book.BookLendingRepository;
import ru.valkeru.libdemo.domain.service.BookLendingService;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookLendingServiceImpl implements BookLendingService {


    private final BookLendingRepository repository;
    private final BookLendingMapper mapper;

    @Override
    public BookLending createLending(BookLendingCreateRequest lendingCreateRequest) {
        BookLending bookLending = mapper.create(lendingCreateRequest);

        return repository.persist(bookLending);
    }

    @Override
    public Page<BookLendingListProjection> getLendingListForUser(User user, Pageable pageable) {
        Sort sort = Sort.by(Sort.Direction.DESC, "b.createdAt");
        Pageable request = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), sort);

        return repository.getListByUser(user, request);
    }

    @Override
    public void cancelReservation(UUID id) {
        BookLending lending = repository.findById(id)
            .orElseThrow(NotFoundDomainException::lending);

        handleCancellation(lending);
    }

    @Override
    public void cancelReservation(UUID id, User user) {
        BookLending lending = repository.findByIdAndLibraryCardUser(id, user)
            .orElseThrow(NotFoundDomainException::lending);

        handleCancellation(lending);
    }

    @Override
    public BookLendingProjection getLending(UUID id) {
        return repository.findByIdAsProjection(id)
            .orElseThrow(NotFoundDomainException::lending);
    }

    @Override
    public BookLending getLending(UUID id, LendingStatus lendingStatus) {
        BookLending lending = repository.findById(id)
            .orElseThrow(NotFoundDomainException::lending);

        if (lendingStatus != null && !lendingStatus.equals(lending.getStatus())) {
            throw ConflictDomainException.notValidLendingStatus();
        }

        return lending;
    }

    @Override
    public void update(BookLendingUpdateRequest request, BookLending lending) {
        mapper.update(request, lending);

        repository.merge(lending);
    }

    @Override
    public BookLendingProjection getLendingForUser(UUID id, User user) {
        return repository.findByIdAndUser(id, user)
            .orElseThrow(NotFoundDomainException::lending);
    }

    @Override
    public void returnBook(BookLending lending) {
        lending.setStatus(LendingStatus.RETURNED);
        lending.setReturnedAt(Instant.now());

        repository.merge(lending);
    }

    private void handleCancellation(BookLending lending) {
        if (!LendingStatus.RESERVED.equals(lending.getStatus())) {
            throw ConflictDomainException.notValidLendingStatus();
        }

        lending.setStatus(LendingStatus.CANCELLED);
        lending.setReturnDueDate(null);

        repository.merge(lending);
    }
}
