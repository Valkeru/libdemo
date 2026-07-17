package ru.valkeru.libdemo.application;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.valkeru.libdemo.model.dto.book.BookDto;
import ru.valkeru.libdemo.model.dto.book.BookInstanceCreateDto;
import ru.valkeru.libdemo.model.dto.book.BookInstanceListDto;
import ru.valkeru.libdemo.model.dto.book.BookInstanceViewDto;
import ru.valkeru.libdemo.model.dto.book.BookInstancePatchDto;
import ru.valkeru.libdemo.model.dto.security.LibraryPrincipal;
import ru.valkeru.libdemo.model.request.book.BookFilter;
import ru.valkeru.libdemo.model.transport.BookListDto;

import java.util.UUID;

public interface BookApplicationService {

    UUID createBook(BookDto bookDto);

    void updateBook(UUID id, BookDto bookDto);

    Page<BookListDto> getAllBooks(BookFilter filter, Pageable pageable);

    BookDto getBookById(UUID id);

    void deleteBookById(UUID id);

    UUID createBookInstance(BookInstanceCreateDto dto);

    Page<BookInstanceListDto> getBookInstances(UUID bookId, Pageable pageable);

    BookInstanceViewDto getBookInstance(UUID id);

    BookInstanceViewDto updateBookInstance(UUID id, BookInstancePatchDto dto, LibraryPrincipal principal);
}

