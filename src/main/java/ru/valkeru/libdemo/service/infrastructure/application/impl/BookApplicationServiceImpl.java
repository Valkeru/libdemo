package ru.valkeru.libdemo.service.infrastructure.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.valkeru.libdemo.mapper.BookMapper;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.dto.BookDto;
import ru.valkeru.libdemo.model.dto.CycleDto;
import ru.valkeru.libdemo.model.dto.SeriesDto;
import ru.valkeru.libdemo.model.entity.Author;
import ru.valkeru.libdemo.model.entity.Book;
import ru.valkeru.libdemo.model.entity.Cycle;
import ru.valkeru.libdemo.model.entity.Series;
import ru.valkeru.libdemo.model.request.book.BookFilter;
import ru.valkeru.libdemo.model.transport.BookListDto;
import ru.valkeru.libdemo.repository.jpa.projection.BookShortProjection;
import ru.valkeru.libdemo.service.core.AuthorService;
import ru.valkeru.libdemo.service.core.BookService;
import ru.valkeru.libdemo.service.core.CycleService;
import ru.valkeru.libdemo.service.core.SeriesService;
import ru.valkeru.libdemo.service.infrastructure.application.BookApplicationService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookApplicationServiceImpl implements BookApplicationService {

    private final BookService bookService;
    private final AuthorService authorService;
    private final SeriesService seriesService;
    private final CycleService cycleService;
    private final BookMapper bookMapper;

    @Transactional
    @Override
    public BookDto createOrUpdateBook(UUID id, BookDto bookDto) {
        bookDto.setId(id);

        List<Author> authors = bookDto.getAuthors().stream()
                .map(this::getAuthor)
                .toList();

        Series series = getSeries(bookDto.getSeries());
        Cycle cycle = getCycle(bookDto.getCycle());

        Book book = bookService.createOrUpdateBook(bookDto, authors, series, cycle);

        return bookMapper.toDto(book);
    }

    @Override
    public Page<BookListDto> getAllBooks(BookFilter filter, Pageable pageable) {
        Page<BookShortProjection> allBooks = bookService.getAllBooks(filter, pageable);

        return allBooks.map(bookMapper::toListDto);
    }

    @Override
    public BookDto getBookById(UUID id) {
        return bookService.getBookById(id);
    }

    @Override
    @Transactional
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
