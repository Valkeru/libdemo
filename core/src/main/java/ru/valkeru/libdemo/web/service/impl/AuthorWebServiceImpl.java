package ru.valkeru.libdemo.web.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import ru.valkeru.libdemo.mapper.AuthorMapper;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.entity.Author;
import ru.valkeru.libdemo.model.request.author.AuthorFilter;
import ru.valkeru.libdemo.service.base.AuthorService;
import ru.valkeru.libdemo.web.service.base.AuthorWebService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthorWebServiceImpl implements AuthorWebService {

    private final AuthorService authorService;
    private final AuthorMapper authorMapper;

    @Override
    public AuthorDto createOrUpdateAuthor(AuthorDto authorDto) {
        Author author = authorService.createOrUpdateAuthor(authorDto);

        return authorMapper.toDto(author);
    }

    @Override
    public PagedModel<AuthorDto> listAllAuthors(AuthorFilter filter, Pageable pageable) {
        Page<Author> authors = authorService.getAuthors(filter, pageable);

        return new PagedModel<>(authors.map(authorMapper::toDto));
    }

    @Cacheable("author")
    @Override
    public AuthorDto getAuthorById(UUID id) {
        Author author = authorService.getAuthorById(id);

        return authorMapper.toDto(author);
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
