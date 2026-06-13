package ru.valkeru.libdemo.web.controller.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.valkeru.libdemo.constants.CustomHeaders;
import ru.valkeru.libdemo.model.dto.BookDto;
import ru.valkeru.libdemo.service.application.BookApplicationService;
import ru.valkeru.libdemo.web.api.service.BookServiceApi;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class BookServiceController implements BookServiceApi {

    private final BookApplicationService bookService;

    @Override
    public ResponseEntity<Void> addBook(BookDto book) {
        UUID id = bookService.createBook(book);

        return ResponseEntity.status(HttpStatus.CREATED)
            .header(CustomHeaders.RESOURCE_ID, id.toString())
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
}
