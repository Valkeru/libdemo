package ru.valkeru.libdemo.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.valkeru.libdemo.model.dto.book.BookDto;
import ru.valkeru.libdemo.domain.entity.Author;
import ru.valkeru.libdemo.domain.entity.Book;
import ru.valkeru.libdemo.domain.entity.Cycle;
import ru.valkeru.libdemo.domain.entity.Series;
import ru.valkeru.libdemo.model.request.book.BookFilter;
import ru.valkeru.libdemo.domain.projection.BookShortProjection;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookService {

    Book createBook(BookDto bookDto, List<Author> authors, Series series, Cycle cycle);

    void updateBook(BookDto bookDto, List<Author> authors, Series series, Cycle cycle, Book book);

    Page<BookShortProjection> getAllBooks(BookFilter filter, Pageable pageable);

    Book getBookById(UUID id);

    Book getReference(UUID id);

    void deleteBookById(UUID id);

    Optional<Book> findById(UUID id);
}
