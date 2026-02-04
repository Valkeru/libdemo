package ru.valkeru.libdemo.service.core;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.entity.Author;
import ru.valkeru.libdemo.model.request.author.AuthorFilter;

import java.util.UUID;

public interface AuthorService {

    Author createOrUpdateAuthor(AuthorDto authorDto);

    Page<Author> getAuthors(AuthorFilter filter, Pageable pageable);

    Author getAuthorById(UUID id);

    void deleteAuthorById(UUID id);

    void reindexAuthors();
}
