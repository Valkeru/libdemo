package ru.valkeru.libdemo.domain.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.valkeru.libdemo.domain.entity.Book;
import ru.valkeru.libdemo.domain.entity.BookInstance;
import ru.valkeru.libdemo.domain.exception.DomainNotFoundException;
import ru.valkeru.libdemo.domain.repository.jpa.book.BookInstanceRepository;
import ru.valkeru.libdemo.domain.service.BookInstanceService;
import ru.valkeru.libdemo.mapper.BookInstanceMapper;
import ru.valkeru.libdemo.model.dto.book.BookInstanceCreateDto;
import ru.valkeru.libdemo.model.dto.book.BookInstanceViewDto;

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
            .orElseThrow(DomainNotFoundException::bookInstance);
    }

    @Override
    public void updateInstance(UUID id, BookInstanceCreateDto dto) {
        BookInstance instance = repository.findById(id)
            .orElseThrow(DomainNotFoundException::bookInstance);

        mapper.update(dto, instance);

        repository.merge(instance);
    }
}
