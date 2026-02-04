package ru.valkeru.libdemo.service.core.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.valkeru.libdemo.exception.impl.BookNotFoundException;
import ru.valkeru.libdemo.mapper.BookMapper;
import ru.valkeru.libdemo.model.dto.BookDto;
import ru.valkeru.libdemo.model.entity.Author;
import ru.valkeru.libdemo.model.entity.Book;
import ru.valkeru.libdemo.model.entity.Cycle;
import ru.valkeru.libdemo.model.entity.Series;
import ru.valkeru.libdemo.repository.jpa.BookRepository;
import ru.valkeru.libdemo.service.core.BookService;
import ru.valkeru.libdemo.util.SqlUtil;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

    private final BookRepository repository;
    private final BookMapper mapper;

    @Override
    public Book createOrUpdateBook(BookDto bookDto, List<Author> authors, Series series, Cycle cycle) {
        Book book = getBookEntity(bookDto);

        mapper.updateBookEntity(bookDto, authors, series, cycle, book);

        return bookDto.getId() == null
                ? repository.persist(book)
                : repository.update(book);
    }

    @Override
    public Collection<BookDto> getAllBooks() {
        Page<Book> bookPage = repository.findAll(Pageable.unpaged(SqlUtil.sortByCreatedAtAsc()));

        return mapper.toDtoCollection(bookPage.toList());
    }

    @Override
    public BookDto getBookById(UUID id) {
        Book bookEntity = getBookEntity(id);

        return mapper.toDto(bookEntity);
    }

    @Override
    public void deleteBookById(UUID id) {
        if (repository.deleteBookById(id) == 0) {
            throw BookNotFoundException.bookNotFound(id);
        }
    }

    private Book getBookEntity(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> BookNotFoundException.bookNotFound(id));
    }

    private Book getBookEntity(BookDto dto) {
        return Optional.ofNullable(dto.getId())
                .map(this::getBookEntity)
                .orElse(new Book());
    }
}
