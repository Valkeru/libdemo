package ru.valkeru.libdemo.service.http.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.valkeru.libdemo.model.dto.BookDto;
import ru.valkeru.libdemo.service.BookService;
import ru.valkeru.libdemo.service.http.BookHttpService;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookHttpServiceImpl implements BookHttpService {

    BookService bookService;

    @Override
    public BookDto createOrUpdateBook(Long id, BookDto bookDto) {
        bookDto.setId(id);

        return bookService.createOrUpdateBook(bookDto);
    }

    @Override
    public Collection<BookDto> getAllBooks() {
        return bookService.getAllBooks();
    }

    @Override
    public BookDto getBookById(Long id) {
        return bookService.getBookById(id);
    }

    @Override
    public void deleteBookById(Long id) {
        bookService.deleteBookById(id);
    }
}
