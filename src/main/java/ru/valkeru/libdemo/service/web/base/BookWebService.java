package ru.valkeru.libdemo.service.web.base;

import ru.valkeru.libdemo.model.dto.BookDto;

import java.util.Collection;

public interface BookWebService {

    BookDto createOrUpdateBook(Long id, BookDto bookDto);

    Collection<BookDto> getAllBooks();

    BookDto getBookById(Long id);

    void deleteBookById(Long id);
}

