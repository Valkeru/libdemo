package ru.valkeru.libdemo.web.service.base;

import ru.valkeru.libdemo.model.dto.BookDto;

import java.util.Collection;
import java.util.UUID;

public interface BookWebService {

    BookDto createOrUpdateBook(UUID id, BookDto bookDto);

    Collection<BookDto> getAllBooks();

    BookDto getBookById(UUID id);

    void deleteBookById(UUID id);
}

