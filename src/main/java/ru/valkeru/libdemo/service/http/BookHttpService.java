package ru.valkeru.libdemo.service.http;

import ru.valkeru.libdemo.model.dto.BookDto;

import java.util.Collection;

public interface BookHttpService {

    BookDto createOrUpdateBook(Long id, BookDto bookDto);

    Collection<BookDto> getAllBooks();

    BookDto getBookById(Long id);

    void deleteBookById(Long id);
}

