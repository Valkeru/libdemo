package ru.valkeru.libdemo.service.impl;

import jakarta.annotation.Nonnull;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.valkeru.libdemo.exception.impl.AuthorNotFoundException;
import ru.valkeru.libdemo.exception.impl.AuthorViolationException;
import ru.valkeru.libdemo.helper.BookHelper;
import ru.valkeru.libdemo.mapper.AuthorMapper;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.entity.Author;
import ru.valkeru.libdemo.model.entity.Book;
import ru.valkeru.libdemo.repository.AuthorRepository;
import ru.valkeru.libdemo.service.AuthorService;
import ru.valkeru.libdemo.util.SqlUtil;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthorServiceImpl implements AuthorService {

    AuthorRepository authorRepository;
    AuthorMapper authorMapper;

    @Transactional
    @Override
    public AuthorDto createOrUpdateAuthor(AuthorDto authorDto) {
        Author author = getAuthorEntity(authorDto);
        authorMapper.updateAuthor(authorDto, author);

        return authorMapper.toDto(authorRepository.save(author));
    }

    @Override
    @Cacheable("authors_all")
    public Collection<AuthorDto> getAuthors() {
        return authorRepository.getAuthors(SqlUtil.sortByCreatedAtAsc());
    }

    @Override
    public Collection<Long> checkNotExistedIds(Collection<Long> testedIds) {
        return authorRepository.getIdNotExisted(testedIds);
    }

    @Nonnull
    @Override
    public Collection<Author> getAllById(Collection<Long> ids) {
        return authorRepository.getAuthorsByIdIn(ids, SqlUtil.sortByIdAsc());
    }

    @Nonnull
    @Override
    public AuthorDto getAuthorById(Long id) {
        return authorMapper.toDto(getAuthorEntity(id));
    }

    @Override
    @Transactional
    public Author getAuthor(Long id) {
        Author authorEntity = getAuthorEntity(id);
        Set<Book> books = authorEntity.getBooks();
        books.forEach(book -> {
            log.debug(book.toString());
        });

        return authorEntity;
    }

    @Transactional
    @Override
    public void deleteAuthorById(Long id) {
        if (BookHelper.authorHasBooks(id)) {
            throw AuthorViolationException.unableToDeleteHasBooks();
        }

        if (authorRepository.deleteAuthorById(id) == 0) {
            throw AuthorNotFoundException.authorNotFound(id);
        }
    }

    private Author getAuthorEntity(AuthorDto authorDto) {
        return Optional.ofNullable(authorDto.getId())
                .map(this::getAuthorEntity)
                .orElse(new Author());
    }

    private Author getAuthorEntity(Long id) {
        return authorRepository.findById(id)
                .orElseThrow(() -> AuthorNotFoundException.authorNotFound(id));
    }
}
