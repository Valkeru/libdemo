package ru.valkeru.libdemo.service.http;

import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.entity.Author;

import java.util.Collection;

public interface AuthorHttpService {

    AuthorDto createOrUpdateAuthor(AuthorDto authorDto);

    Collection<AuthorDto> listAllAuthors();

    AuthorDto getAuthorById(Long id);

    void deleteAuthor(Long id);

    Author getAuthorEntity(Long id);
}
