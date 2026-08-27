package ru.valkeru.libdemo.domain.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.valkeru.libdemo.domain.entity.Book;
import ru.valkeru.libdemo.domain.entity.BookInstance;
import ru.valkeru.libdemo.model.dto.book.BookInstanceCreateDto;

import java.util.UUID;

public interface BookInstanceService {

    BookInstance createInstance(BookInstanceCreateDto dto, Book book);

    BookInstance getBookInstance(UUID id);

    BookInstance updateInstance(UUID id, BookInstanceCreateDto dto);

    Page<BookInstance> findByBookId(UUID bookId, Pageable pageable);

    BookInstance getAvailableInstance(Book book);
}
