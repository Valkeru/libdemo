package ru.valkeru.libdemo.service.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.valkeru.libdemo.exception.impl.BookNotFoundException;
import ru.valkeru.libdemo.mapper.BookMapper;
import ru.valkeru.libdemo.model.dto.BookDto;
import ru.valkeru.libdemo.model.entity.Book;
import ru.valkeru.libdemo.repository.jpa.BookRepository;
import ru.valkeru.libdemo.service.BookService;
import ru.valkeru.libdemo.util.SqlUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class BookServiceImpl implements BookService {

    BookRepository bookRepository;
    BookMapper bookMapper;

    @Transactional
    @Override
    public BookDto createOrUpdateBook(BookDto bookDto) {
        Book book = getBookEntity(bookDto);

        bookMapper.updateBookEntity(bookDto, null, null, new ArrayList<>(), book);

        return bookMapper.toDto(bookRepository.save(book));
    }

    @Transactional(readOnly = true)
    @Override
    public Collection<BookDto> getAllBooks() {
        return bookMapper.toDtoCollection(bookRepository.findAll(SqlUtil.sortByCreatedAtAsc()));
    }

    @Transactional(readOnly = true)
    @Override
    public BookDto getBookById(Long id) {
        Book bookEntity = getBookEntity(id);

        return bookMapper.toDto(bookEntity);
    }

    @Transactional
    @Override
    public void deleteBookById(Long id) {
        if (bookRepository.deleteBookById(id) == 0) {
            throw BookNotFoundException.bookNotFound(id);
        }
    }

    @Override
    public int countBooksByAuthorId(Long authorId) {
        return bookRepository.countBooksByAuthorsId(authorId);
    }

    @Override
    public int countBooksByCycleId(Long cycleId) {
        return bookRepository.countBooksByCycleId(cycleId);
    }

    private Book getBookEntity(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> BookNotFoundException.bookNotFound(id));
    }

    private Book getBookEntity(BookDto dto) {
        return Optional.ofNullable(dto.getId())
                .map(this::getBookEntity)
                .orElse(new Book());
    }
}
