package ru.valkeru.libdemo.web.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.dto.BookDto;
import ru.valkeru.libdemo.model.dto.CycleDto;
import ru.valkeru.libdemo.model.dto.SeriesDto;
import ru.valkeru.libdemo.model.entity.Author;
import ru.valkeru.libdemo.model.entity.Cycle;
import ru.valkeru.libdemo.model.entity.Series;
import ru.valkeru.libdemo.service.base.AuthorService;
import ru.valkeru.libdemo.service.base.BookService;
import ru.valkeru.libdemo.service.base.CycleService;
import ru.valkeru.libdemo.service.base.SeriesService;
import ru.valkeru.libdemo.web.service.base.BookWebService;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookWebServiceImpl implements BookWebService {

    private final BookService bookService;
    private final AuthorService authorService;
    private final SeriesService seriesService;
    private final CycleService cycleService;

    @Override
    public BookDto createOrUpdateBook(UUID id, BookDto bookDto) {
        bookDto.setId(id);
        List<Author> authors = bookDto.getAuthors().stream()
                .map(this::getAuthor)
                .toList();
        Series series = getSeries(bookDto.getSeries());
        Cycle cycle = getCycle(bookDto.getCycle());

        return bookService.createOrUpdateBook(bookDto, authors, series, cycle);
    }

    @Override
    public Collection<BookDto> getAllBooks() {
        return bookService.getAllBooks();
    }

    @Override
    public BookDto getBookById(UUID id) {
        return bookService.getBookById(id);
    }

    @Override
    public void deleteBookById(UUID id) {
        bookService.deleteBookById(id);
    }

    private Author getAuthor(AuthorDto dto) {
        return dto != null ? authorService.getAuthorById(dto.getId()) : null;
    }

    private Series getSeries(SeriesDto dto) {
        return dto != null ? seriesService.getSeries(dto.getId()) : null;
    }

    private Cycle getCycle(CycleDto dto) {
        return dto != null ? cycleService.getCycleById(dto.getId()) : null;
    }
}
