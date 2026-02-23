package ru.valkeru.libdemo.service.infrastructure.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.valkeru.libdemo.model.dto.BookDto;
import ru.valkeru.libdemo.model.request.book.BookFilter;
import ru.valkeru.libdemo.model.transport.BookListDto;

import java.util.UUID;

public interface BookApplicationService {

    BookDto createOrUpdateBook(UUID id, BookDto bookDto);

    Page<BookListDto> getAllBooks(BookFilter filter, Pageable pageable);

    BookDto getBookById(UUID id);

    void deleteBookById(UUID id);
}

