package ru.valkeru.libdemo.persistence.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.valkeru.libdemo.exception.impl.BookNotFoundException;
import ru.valkeru.libdemo.mapper.BookMapper;
import ru.valkeru.libdemo.model.dto.book.BookDto;
import ru.valkeru.libdemo.persistence.entity.Author;
import ru.valkeru.libdemo.persistence.entity.Book;
import ru.valkeru.libdemo.persistence.entity.Cycle;
import ru.valkeru.libdemo.persistence.entity.Series;
import ru.valkeru.libdemo.model.request.book.BookFilter;
import ru.valkeru.libdemo.persistence.repository.jpa.book.BookRepository;
import ru.valkeru.libdemo.persistence.projection.BookShortProjection;
import ru.valkeru.libdemo.persistence.service.BookService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository repository;
    private final BookMapper mapper;

    @Override
    public Book createBook(BookDto bookDto, List<Author> authors, Series series, Cycle cycle) {
        Book book = new Book();
        mapper.updateBookEntity(bookDto, authors, series, cycle, book);

        return repository.persist(book);
    }

    @Override
    public void updateBook(BookDto bookDto, List<Author> authors, Series series, Cycle cycle, Book book) {
        mapper.updateBookEntity(bookDto, authors, series, cycle, book);

        repository.merge(book);
    }

    @Override
    public Page<BookShortProjection> getAllBooks(BookFilter filter, Pageable pageable) {
        return repository.findAllBooks(filter, pageable);
    }

    @Override
    public Book getBookById(UUID id) {
        return repository.findById(id)
            .orElseThrow(() -> BookNotFoundException.bookNotFound(id));
    }

    @Override
    public Book getReference(UUID id) {
        if (!repository.existsById(id)) {
            throw BookNotFoundException.bookNotFound(id);
        }

        return repository.getReferenceById(id);
    }

    @Override
    public void deleteBookById(UUID id) {
        if (repository.deleteBookById(id) == 0) {
            throw BookNotFoundException.bookNotFound(id);
        }
    }

    @Override
    public Optional<Book> findById(UUID id) {
        if (!repository.existsById(id)) {
            return Optional.empty();
        }

        return Optional.of(repository.getReferenceById(id));
    }
}
