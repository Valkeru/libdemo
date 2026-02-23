package ru.valkeru.libdemo.repository.jpa.book;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.valkeru.libdemo.model.request.book.BookFilter;
import ru.valkeru.libdemo.repository.jpa.projection.BookShortProjection;

public interface BookDslRepository {

    Page<BookShortProjection> findAllBooks(BookFilter filter, Pageable pageable);
}
