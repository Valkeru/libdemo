package ru.valkeru.libdemo.service.web.impl;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.request.author.AuthorFilter;
import ru.valkeru.libdemo.service.base.AuthorService;
import ru.valkeru.libdemo.service.web.base.AuthorWebService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthorWebServiceImpl implements AuthorWebService {

    AuthorService authorService;

    @Override
    public AuthorDto createOrUpdateAuthor(AuthorDto authorDto) {
        return authorService.createOrUpdateAuthor(authorDto);
    }

    @Override
    public Page<AuthorDto> listAllAuthors(AuthorFilter filter, Pageable pageable) {
        return authorService.getAuthors(filter, pageable);
    }

    @Cacheable("author")
    @Override
    public AuthorDto getAuthorById(UUID id) {
        return authorService.getAuthorById(id);
    }

    @Override
    public void deleteAuthor(UUID id) {
        authorService.deleteAuthorById(id);
    }

    @Override
    public void reindexAuthors() {
        authorService.reindexAuthors();
    }
}
