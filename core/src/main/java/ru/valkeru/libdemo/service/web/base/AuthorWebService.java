package ru.valkeru.libdemo.service.web.base;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.request.author.AuthorFilter;

public interface AuthorWebService {

    AuthorDto createOrUpdateAuthor(AuthorDto authorDto);

    Page<AuthorDto> listAllAuthors(AuthorFilter filter, Pageable pageable);

    AuthorDto getAuthorById(Long id);

    void deleteAuthor(Long id);

    @Async
    void reindexAuthors();
}
