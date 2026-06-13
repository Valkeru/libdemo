package ru.valkeru.libdemo.service.application;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.request.author.AuthorFilter;
import ru.valkeru.libdemo.model.transport.AuthorListDto;

import java.util.UUID;

public interface AuthorApplicationService {

    UUID createAuthor(AuthorDto authorDto);

    void updateAuthor(AuthorDto dto);

    Page<AuthorListDto> listAllAuthors(AuthorFilter filter, Pageable pageable);

    AuthorDto getAuthorById(UUID id);

    void deleteAuthor(UUID id);

    void reindexAuthors();
}
