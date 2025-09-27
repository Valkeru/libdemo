package ru.valkeru.libdemo.service;

import ru.valkeru.libdemo.model.dto.BookDto;

import java.util.Collection;

public interface BookService {

    BookDto createOrUpdateBook(BookDto bookDto);

    Collection<BookDto> getAllBooks();

    BookDto getBookById(Long id);

    void deleteBookById(Long id);
}
