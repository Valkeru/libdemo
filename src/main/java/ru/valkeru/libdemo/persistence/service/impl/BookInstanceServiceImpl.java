package ru.valkeru.libdemo.persistence.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.valkeru.libdemo.persistence.entity.Book;
import ru.valkeru.libdemo.persistence.entity.BookInstance;
import ru.valkeru.libdemo.persistence.enums.LendingStatus;
import ru.valkeru.libdemo.persistence.exception.ConflictPersistenceException;
import ru.valkeru.libdemo.persistence.exception.NotFoundPersistenceException;
import ru.valkeru.libdemo.persistence.repository.jpa.book.BookInstanceRepository;
import ru.valkeru.libdemo.persistence.service.BookInstanceService;
import ru.valkeru.libdemo.mapper.BookInstanceMapper;
import ru.valkeru.libdemo.model.dto.book.BookInstanceCreateDto;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookInstanceServiceImpl implements BookInstanceService {

    private final BookInstanceRepository repository;
    private final BookInstanceMapper mapper;

    @Override
    public BookInstance createInstance(BookInstanceCreateDto dto, Book book) {
        BookInstance instance = new BookInstance();
        mapper.update(dto, book, instance);

        return repository.persist(instance);
    }

    @Override
    public BookInstance getBookInstance(UUID id) {
        return repository.findById(id)
            .orElseThrow(NotFoundPersistenceException::bookInstance);
    }

    @Override
    public BookInstance updateInstance(UUID id, BookInstanceCreateDto dto) {
        BookInstance instance = repository.findById(id)
            .orElseThrow(NotFoundPersistenceException::bookInstance);

        mapper.update(dto, instance);

        return repository.merge(instance);
    }

    @Override
    public Page<BookInstance> findByBookId(UUID bookId, Pageable pageable) {
        Pageable request = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

        return repository.findAllByBookId(bookId, request);
    }

    @Override
    public BookInstance getAvailableInstance(Book book) {
        List<String> lockedStatuses = List.of(LendingStatus.RESERVED.name(), LendingStatus.BORROWED.name());

        UUID availableInstanceId = repository.reserveAvailableInstance(book, lockedStatuses)
            .orElseThrow(ConflictPersistenceException::bookInstanceNotAvailable);

        return repository.getReferenceById(availableInstanceId);
    }
}
