package ru.valkeru.libdemo.web.controller.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.data.web.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.valkeru.libdemo.web.api.v1.BookApi;
import ru.valkeru.libdemo.model.dto.BookDto;
import ru.valkeru.libdemo.service.infrastructure.application.BookApplicationService;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BookController implements BookApi {

    private final BookApplicationService bookService;

    @Override
    public ResponseEntity<PagedModel<BookDto>> getAllBooks() {
        PagedModel<BookDto> allBooks = bookService.getAllBooks();

        return ResponseEntity.ok(allBooks);
    }

    @Override
    public ResponseEntity<BookDto> getBookById(UUID id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }
}
