package ru.valkeru.libdemo.service.infrastructure.application;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.scheduling.annotation.Async;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.request.author.AuthorFilter;
import ru.valkeru.libdemo.model.transport.AuthorListDto;

import java.util.UUID;

public interface AuthorApplicationService {

    AuthorDto createOrUpdateAuthor(AuthorDto authorDto);

    Page<AuthorListDto> listAllAuthors(AuthorFilter filter, Pageable pageable);

    AuthorDto getAuthorById(UUID id);

    void deleteAuthor(UUID id);

    @Async
    void reindexAuthors();
}
