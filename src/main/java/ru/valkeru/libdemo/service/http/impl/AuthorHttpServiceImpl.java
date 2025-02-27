package ru.valkeru.libdemo.service.http.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.entity.Author;
import ru.valkeru.libdemo.service.AuthorService;
import ru.valkeru.libdemo.service.http.AuthorHttpService;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthorHttpServiceImpl implements AuthorHttpService {

    AuthorService authorService;

    @Override
    public AuthorDto createOrUpdateAuthor(AuthorDto authorDto) {
        return authorService.createOrUpdateAuthor(authorDto);
    }

    @Override
    public Collection<AuthorDto> listAllAuthors() {
        Collection<AuthorDto> authors = authorService.getAuthors();

        return authors;
    }

    @Override
    public AuthorDto getAuthorById(Long id) {
        return authorService.getAuthorById(id);
    }

    @Override
    public Author getAuthorEntity(Long id) {
        return authorService.getAuthor(id);
    }

    @Override
    public void deleteAuthor(Long id) {
        authorService.deleteAuthorById(id);
    }
}
