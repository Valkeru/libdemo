package ru.valkeru.libdemo.web.service.base;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.scheduling.annotation.Async;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.request.author.AuthorFilter;

import java.util.UUID;

public interface AuthorWebService {

    AuthorDto createOrUpdateAuthor(AuthorDto authorDto);

    PagedModel<AuthorDto> listAllAuthors(AuthorFilter filter, Pageable pageable);

    AuthorDto getAuthorById(UUID id);

    void deleteAuthor(UUID id);

    @Async
    void reindexAuthors();
}
