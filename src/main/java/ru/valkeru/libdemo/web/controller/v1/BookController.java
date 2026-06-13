package ru.valkeru.libdemo.web.controller.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.valkeru.libdemo.model.request.book.BookFilter;
import ru.valkeru.libdemo.model.transport.BookListDto;
import ru.valkeru.libdemo.web.api.v1.BookApi;
import ru.valkeru.libdemo.model.dto.BookDto;
import ru.valkeru.libdemo.service.application.BookApplicationService;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BookController implements BookApi {

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
}
