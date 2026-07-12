package ru.valkeru.libdemo.web.controller.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import ru.valkeru.libdemo.model.dto.book.BookInstanceCreateDto;
import ru.valkeru.libdemo.model.dto.book.BookInstanceViewDto;
import ru.valkeru.libdemo.model.dto.book.BookInstancePatchDto;
import ru.valkeru.libdemo.model.dto.security.LibraryPrincipal;
import ru.valkeru.libdemo.model.request.book.BookFilter;
import ru.valkeru.libdemo.model.transport.BookListDto;
import ru.valkeru.libdemo.web.api.v1.BookApi;
import ru.valkeru.libdemo.model.dto.book.BookDto;
import ru.valkeru.libdemo.application.BookApplicationService;
import ru.valkeru.libdemo.web.api.service.BookServiceApi;

import java.util.UUID;

import static org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder.on;

@RestController
@RequiredArgsConstructor
public class BookController implements BookApi, BookServiceApi {

    private final BookApplicationService bookService;

    @Override
    public ResponseEntity<Page<BookListDto>> getAllBooks(BookFilter filter, Pageable pageable) {
        Page<BookListDto> allBooks = bookService.getAllBooks(filter, pageable);

        return ResponseEntity.ok(allBooks);
    }

    @Override
    public ResponseEntity<BookDto> getBookById(UUID id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @Override
    public ResponseEntity<Void> addBook(BookDto book) {
        UUID id = bookService.createBook(book);

        return ResponseEntity.created(
                MvcUriComponentsBuilder
                    .fromMethodCall(on(BookApi.class).getBookById(id))
                    .build().toUri()
            )
            .build();
    }

    @Override
    public ResponseEntity<Void> updateBook(UUID id, BookDto book) {
        bookService.updateBook(id, book);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> deleteBookById(UUID id) {
        bookService.deleteBookById(id);

        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<Void> addBookCopy(BookInstanceCreateDto dto) {
        UUID id = bookService.createBookInstance(dto);

        return ResponseEntity.created(
                MvcUriComponentsBuilder
                    .fromMethodCall(on(BookServiceApi.class).getBookCopy(id))
                    .build().toUri()
            )
            .build();
    }

    @Override
    public ResponseEntity<BookInstanceViewDto> getBookCopy(UUID id) {
        return ResponseEntity.ok(bookService.getBookInstance(id));
    }

    @Override
    public ResponseEntity<Void> updateBookCopy(UUID id, BookInstancePatchDto dto, LibraryPrincipal principal) {
        bookService.updateBookInstance(id, dto, principal);
        return null;
    }
}
