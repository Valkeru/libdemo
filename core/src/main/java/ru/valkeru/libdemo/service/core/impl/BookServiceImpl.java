package ru.valkeru.libdemo.service.core.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookServiceImpl implements BookService {

    BookRepository bookRepository;
    BookMapper bookMapper;

    @Transactional
    @Override
    public BookDto createOrUpdateBook(BookDto bookDto, List<Author> authors, Series series, Cycle cycle) {
        Book book = getBookEntity(bookDto);

        bookMapper.updateBookEntity(bookDto, authors, series, cycle, book);

        return bookMapper.toDto(bookRepository.save(book));
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<BookDto> getAllBooks() {
        return bookMapper.toDtoCollection(bookRepository.findAll(SqlUtil.sortByCreatedAtAsc()));
    }

    @Transactional(readOnly = true)
    @Override
    public BookDto getBookById(UUID id) {
        Book bookEntity = getBookEntity(id);

        return bookMapper.toDto(bookEntity);
    }

    @Transactional
    @Override
    public void deleteBookById(UUID id) {
        if (bookRepository.deleteBookById(id) == 0) {
            throw BookNotFoundException.bookNotFound(id);
        }
    }

    private Book getBookEntity(UUID id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> BookNotFoundException.bookNotFound(id));
    }

    private Book getBookEntity(BookDto dto) {
        return Optional.ofNullable(dto.getId())
                .map(this::getBookEntity)
                .orElse(new Book());
    }
}
