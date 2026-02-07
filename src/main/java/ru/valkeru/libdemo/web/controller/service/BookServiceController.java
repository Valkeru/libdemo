package ru.valkeru.libdemo.web.controller.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.valkeru.libdemo.model.dto.BookDto;
import ru.valkeru.libdemo.service.infrastructure.application.BookApplicationService;
import ru.valkeru.libdemo.web.api.service.BookServiceApi;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BookServiceController implements BookServiceApi {

    private final BookApplicationService bookService;

    @Override
    public ResponseEntity<BookDto> addBook(BookDto book) {
        BookDto result = bookService.createOrUpdateBook(null, book);

        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }

    @Override
    public ResponseEntity<BookDto> updateBook(UUID id, BookDto book) {
        BookDto result = bookService.createOrUpdateBook(id, book);

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Void> deleteBookById(UUID id) {
        bookService.deleteBookById(id);

        return ResponseEntity.noContent().build();
    }
}
