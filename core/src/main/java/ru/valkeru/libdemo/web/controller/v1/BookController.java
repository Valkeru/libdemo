package ru.valkeru.libdemo.web.controller.v1;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import ru.valkeru.libdemo.web.api.v1.BookApi;
import ru.valkeru.libdemo.model.dto.BookDto;
import ru.valkeru.libdemo.service.web.base.BookWebService;

import java.util.Collection;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookController implements BookApi {

    BookWebService bookService;

    @Override
    public ResponseEntity<BookDto> addBook(BookDto book) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(bookService.createOrUpdateBook(null, book));
    }

    @Override
    public ResponseEntity<BookDto> updateBook(Long id, BookDto book) {
        return ResponseEntity.ok(bookService.createOrUpdateBook(id, book));
    }

    @Override
    public ResponseEntity<Collection<BookDto>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @Override
    public ResponseEntity<BookDto> getBookById(Long id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }

    @Override
    public ResponseEntity<Void> deleteBookById(Long id) {
        bookService.deleteBookById(id);

        return ResponseEntity.noContent().build();
    }
}
