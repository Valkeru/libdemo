package ru.valkeru.libdemo.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.valkeru.libdemo.domain.entity.BookInstance;
import ru.valkeru.libdemo.domain.service.BookInstanceService;
import ru.valkeru.libdemo.infrastructure.security.PermissionService;
import ru.valkeru.libdemo.exception.impl.BookNotFoundException;
import ru.valkeru.libdemo.exception.impl.CycleNotFoundException;
import ru.valkeru.libdemo.exception.impl.SeriesNotFoundException;
import ru.valkeru.libdemo.mapper.BookInstanceMapper;
import ru.valkeru.libdemo.mapper.BookMapper;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.dto.book.BookDto;
import ru.valkeru.libdemo.model.dto.CycleDto;
import ru.valkeru.libdemo.model.dto.book.BookInstanceCreateDto;
import ru.valkeru.libdemo.model.dto.book.BookInstanceViewDto;
import ru.valkeru.libdemo.model.dto.book.BookInstancePatchDto;
import ru.valkeru.libdemo.model.dto.security.LibraryPrincipal;
import ru.valkeru.libdemo.model.dto.series.SeriesDto;
import ru.valkeru.libdemo.domain.entity.Author;
import ru.valkeru.libdemo.domain.entity.Book;
import ru.valkeru.libdemo.domain.entity.Cycle;
import ru.valkeru.libdemo.domain.entity.Series;
import ru.valkeru.libdemo.model.request.book.BookFilter;
import ru.valkeru.libdemo.model.transport.BookListDto;
import ru.valkeru.libdemo.domain.projection.BookShortProjection;
import ru.valkeru.libdemo.domain.service.AuthorService;
import ru.valkeru.libdemo.domain.service.BookService;
import ru.valkeru.libdemo.domain.service.CycleService;
import ru.valkeru.libdemo.domain.service.SeriesService;
import ru.valkeru.libdemo.application.BookApplicationService;

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
    private final BookInstanceService bookInstanceService;
    private final PermissionService permissionService;
    private final BookInstanceMapper bookInstanceMapper;

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
    public UUID createBookInstance(BookInstanceCreateDto dto) {
        Book book = bookService.getReference(dto.getBookId());

        return bookInstanceService.createInstance(dto, book).getId();
    }

    @Override
    @Transactional
    public void updateBookInstance(UUID id, BookInstancePatchDto patchDto, LibraryPrincipal principal) {
        permissionService.checkPatch(principal, patchDto);
        BookInstanceViewDto dto = bookMapper.toDto(patchDto);

        bookInstanceService.updateInstance(id, dto);
    }

    @Override
    public BookInstanceViewDto getBookInstance(UUID id) {
        BookInstance instance = bookInstanceService.getBookInstance(id);

        return bookInstanceMapper.toDto(instance);
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
        Book book = bookService.getBookById(id);

        return bookMapper.toDto(book);
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
