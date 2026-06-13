package ru.valkeru.libdemo.service.core;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.valkeru.libdemo.model.dto.BookDto;
import ru.valkeru.libdemo.model.entity.Author;
import ru.valkeru.libdemo.model.entity.Book;
import ru.valkeru.libdemo.model.entity.Cycle;
import ru.valkeru.libdemo.model.entity.Series;
import ru.valkeru.libdemo.model.request.book.BookFilter;
import ru.valkeru.libdemo.repository.jpa.book.projection.BookShortProjection;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BookService {

    Book createBook(BookDto bookDto, List<Author> authors, Series series, Cycle cycle);

    void updateBook(BookDto bookDto, List<Author> authors, Series series, Cycle cycle, Book book);

    Page<BookShortProjection> getAllBooks(BookFilter filter, Pageable pageable);

    BookDto getBookById(UUID id);

    void deleteBookById(UUID id);

    Optional<Book> findById(UUID id);
}
