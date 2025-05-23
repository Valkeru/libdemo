package ru.valkeru.libdemo.service.http;

import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.request.author.AuthorFilter;

import java.util.Collection;

public interface AuthorHttpService {

    AuthorDto createOrUpdateAuthor(AuthorDto authorDto);

    Collection<AuthorDto> listAllAuthors(AuthorFilter filter, Pageable pageable);

    AuthorDto getAuthorById(Long id);

    void deleteAuthor(Long id);

    @Async
    void reindexAuthors();
}
