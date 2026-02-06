package ru.valkeru.libdemo.service.infrastructure.application;

import ru.valkeru.libdemo.model.dto.BookDto;

import java.util.Collection;
import java.util.UUID;

public interface BookApplicationService {

    BookDto createOrUpdateBook(UUID id, BookDto bookDto);

    Collection<BookDto> getAllBooks();

    BookDto getBookById(UUID id);

    void deleteBookById(UUID id);
}

