package ru.valkeru.libdemo.service;

import jakarta.annotation.Nonnull;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.entity.Author;

import java.util.Collection;

public interface AuthorService {

    AuthorDto createOrUpdateAuthor(AuthorDto authorDto);

    Collection<AuthorDto> getAuthors();

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

    Author getAuthor(Long id);
}
