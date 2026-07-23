package ru.valkeru.libdemo.persistence.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.valkeru.libdemo.mapper.BookLendingMapper;
import ru.valkeru.libdemo.model.dto.internal.BookLendingCreateRequest;
import ru.valkeru.libdemo.model.dto.internal.BookLendingUpdateRequest;
import ru.valkeru.libdemo.persistence.entity.BookLending;
import ru.valkeru.libdemo.persistence.entity.user.User;
import ru.valkeru.libdemo.persistence.enums.LendingStatus;
import ru.valkeru.libdemo.persistence.exception.ConflictPersistenceException;
import ru.valkeru.libdemo.persistence.exception.NotFoundPersistenceException;
import ru.valkeru.libdemo.persistence.projection.BookLendingListProjection;
import ru.valkeru.libdemo.persistence.projection.BookLendingProjection;
import ru.valkeru.libdemo.persistence.repository.jpa.book.BookLendingRepository;
import ru.valkeru.libdemo.persistence.service.BookLendingService;

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
            .orElseThrow(NotFoundPersistenceException::lending);

        handleCancellation(lending);
    }

    @Override
    public void cancelReservation(UUID id, User user) {
        BookLending lending = repository.findByIdAndReadersCardUser(id, user)
            .orElseThrow(NotFoundPersistenceException::lending);

        handleCancellation(lending);
    }

    @Override
    public BookLendingProjection getLending(UUID id) {
        return repository.findByIdAsProjection(id)
            .orElseThrow(NotFoundPersistenceException::lending);
    }

    @Override
    public BookLending getLending(UUID id, LendingStatus lendingStatus) {
        BookLending lending = repository.findById(id)
            .orElseThrow(NotFoundPersistenceException::lending);

        if (lendingStatus != null && !lendingStatus.equals(lending.getStatus())) {
            throw ConflictPersistenceException.notValidLendingStatus();
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
            .orElseThrow(NotFoundPersistenceException::lending);
    }

    @Override
    public void returnBook(BookLending lending) {
        lending.setStatus(LendingStatus.RETURNED);
        lending.setReturnedAt(Instant.now());

        repository.merge(lending);
    }

    private void handleCancellation(BookLending lending) {
        if (!LendingStatus.RESERVED.equals(lending.getStatus())) {
            throw ConflictPersistenceException.notValidLendingStatus();
        }

        lending.setStatus(LendingStatus.CANCELLED);
        lending.setReturnDueDate(null);

        repository.merge(lending);
    }
}
