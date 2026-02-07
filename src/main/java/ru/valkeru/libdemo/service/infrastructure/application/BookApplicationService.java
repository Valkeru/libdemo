package ru.valkeru.libdemo.service.infrastructure.application;

import org.springframework.data.web.PagedModel;
import ru.valkeru.libdemo.model.dto.BookDto;

import java.util.UUID;

public interface BookApplicationService {

    BookDto createOrUpdateBook(UUID id, BookDto bookDto);

    PagedModel<BookDto> getAllBooks();

    BookDto getBookById(UUID id);

    void deleteBookById(UUID id);
}

