package ru.valkeru.libdemo.service.core;

import ru.valkeru.libdemo.model.dto.BookDto;
import ru.valkeru.libdemo.model.entity.Author;
import ru.valkeru.libdemo.model.entity.Book;
import ru.valkeru.libdemo.model.entity.Cycle;
import ru.valkeru.libdemo.model.entity.Series;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface BookService {

    Book createOrUpdateBook(BookDto bookDto, List<Author> authors, Series series, Cycle cycle);

    Collection<BookDto> getAllBooks();

    BookDto getBookById(UUID id);

    void deleteBookById(UUID id);
}
