package ru.valkeru.libdemo.service.base;

import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.request.author.AuthorFilter;

import java.util.UUID;

public interface AuthorService {

    AuthorDto createOrUpdateAuthor(AuthorDto authorDto);

    Page<AuthorDto> getAuthors(AuthorFilter filter, Pageable pageable);

    @Nonnull
    AuthorDto getAuthorById(UUID id);

    void deleteAuthorById(UUID id);

    void reindexAuthors();
}
