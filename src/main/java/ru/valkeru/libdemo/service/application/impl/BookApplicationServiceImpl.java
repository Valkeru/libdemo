package ru.valkeru.libdemo.service.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.valkeru.libdemo.exception.impl.BookNotFoundException;
import ru.valkeru.libdemo.exception.impl.CycleNotFoundException;
import ru.valkeru.libdemo.exception.impl.SeriesNotFoundException;
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
import ru.valkeru.libdemo.repository.jpa.book.projection.BookShortProjection;
import ru.valkeru.libdemo.service.core.AuthorService;
import ru.valkeru.libdemo.service.core.BookService;
import ru.valkeru.libdemo.service.core.CycleService;
import ru.valkeru.libdemo.service.core.SeriesService;
import ru.valkeru.libdemo.service.application.BookApplicationService;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookApplicationServiceImpl implements BookApplicationService {

    private final BookService bookService;
    private final AuthorService authorService;
    private final SeriesService seriesService;
    private final CycleService cycleService;
    private final BookMapper bookMapper;

    @Override
    @Transactional
    public UUID createBook(BookDto bookDto) {
        List<Author> authors = getAuthors(bookDto);

        Series series = getSeries(bookDto.getSeries());
        Cycle cycle = getCycle(bookDto.getCycle());

        return bookService.createBook(bookDto, authors, series, cycle).getId();
    }

    @Override
    @Transactional
    public void updateBook(UUID id, BookDto bookDto) {
        List<Author> authors = getAuthors(bookDto);

        Series series = getSeries(bookDto.getSeries());
        Cycle cycle = getCycle(bookDto.getCycle());

        Book book = bookService.findById(id)
            .orElseThrow(() -> BookNotFoundException.bookNotFound(id));

        bookService.updateBook(bookDto, authors, series, cycle, book);
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

    private Series getSeries(SeriesDto dto) {
        UUID id = Optional.ofNullable(dto)
                .map(SeriesDto::getId)
                    .orElse(null);

        if (id == null) {
            return null;
        }

        return seriesService.getReference(id)
            .orElseThrow(() -> SeriesNotFoundException.seriesNotFound(id));
    }

    private Cycle getCycle(CycleDto dto) {
        UUID id = Optional.ofNullable(dto)
            .map(CycleDto::getId)
            .orElse(null);

        if (id == null) {
            return null;
        }

        return cycleService.getReference(id)
            .orElseThrow(() -> CycleNotFoundException.cycleNotFound(id));
    }

    private List<Author> getAuthors(BookDto bookDto) {
        Set<UUID> authorIdList = bookDto.getAuthors().stream()
            .map(AuthorDto::getId)
            .collect(Collectors.toSet());

        return authorService.getByIdList(authorIdList);
    }
}
