package ru.valkeru.libdemo.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.valkeru.libdemo.domain.entity.Book;
import ru.valkeru.libdemo.domain.entity.BookInstance;
import ru.valkeru.libdemo.domain.enums.LendingStatus;
import ru.valkeru.libdemo.domain.exception.ConflictDomainException;
import ru.valkeru.libdemo.domain.exception.NotFoundDomainException;
import ru.valkeru.libdemo.domain.repository.jpa.book.BookInstanceRepository;
import ru.valkeru.libdemo.domain.service.BookInstanceService;
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
            .orElseThrow(NotFoundDomainException::bookInstance);
    }

    @Override
    public BookInstance updateInstance(UUID id, BookInstanceCreateDto dto) {
        BookInstance instance = repository.findById(id)
            .orElseThrow(NotFoundDomainException::bookInstance);

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
            .orElseThrow(ConflictDomainException::bookInstanceNotAvailable);

        return repository.getReferenceById(availableInstanceId);
    }
}
