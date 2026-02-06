package ru.valkeru.libdemo.service.infrastructure.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedModel;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.valkeru.libdemo.mapper.AuthorMapper;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.entity.Author;
import ru.valkeru.libdemo.model.request.author.AuthorFilter;
import ru.valkeru.libdemo.service.core.AuthorService;
import ru.valkeru.libdemo.service.infrastructure.application.AuthorApplicationService;
import ru.valkeru.libdemo.service.infrastructure.provider.indexing.AuthorIndexingProvider;
import ru.valkeru.libdemo.service.infrastructure.provider.search.AuthorSearchProvider;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorApplicationServiceImpl implements AuthorApplicationService {

    private final AuthorService authorService;
    private final AuthorSearchProvider authorSearchProvider;
    private final AuthorIndexingProvider indexingProvider;
    private final AuthorMapper authorMapper;

    @Transactional
    @Override
    public AuthorDto createOrUpdateAuthor(AuthorDto authorDto) {
        Author author = authorService.createOrUpdateAuthor(authorDto);

        return authorMapper.toDto(author);
    }

    @Override
    public PagedModel<AuthorDto> listAllAuthors(AuthorFilter filter, Pageable pageable) {
        return authorSearchProvider.listAllAuthors(filter, pageable);
    }

    @Cacheable("author")
    @Override
    public AuthorDto getAuthorById(UUID id) {
        Author author = authorService.getAuthorById(id);

        return authorMapper.toDto(author);
    }

    @Transactional
    @Override
    public void deleteAuthor(UUID id) {
        authorService.deleteAuthorById(id);
    }

    @Override
    public void reindexAuthors() {
        indexingProvider.reindexAuthors();
    }
}
