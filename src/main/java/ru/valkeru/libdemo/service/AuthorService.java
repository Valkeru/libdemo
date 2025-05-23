package ru.valkeru.libdemo.service;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Pageable;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.entity.Author;
import ru.valkeru.libdemo.model.request.author.AuthorFilter;

import java.util.Collection;

public interface AuthorService {

    AuthorDto createOrUpdateAuthor(AuthorDto authorDto);

    Collection<AuthorDto> getAuthors(AuthorFilter filter, Pageable pageable);

    /**
     * Проверка отсутствия записей с переданными ID
     *
     * @param testedIds Проверяемые идентификаторы
     * @return ID, отсутствующие в базе
     */
    Collection<Long> checkNotExistedIds(Collection<Long> testedIds);

    @Nonnull
    Collection<Author> getAllById(Collection<Long> ids);

    @Nonnull
    AuthorDto getAuthorById(Long id);

    void deleteAuthorById(Long id);

    void reindexAuthors();
}
