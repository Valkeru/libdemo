package ru.valkeru.libdemo.service.infrastructure.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.valkeru.libdemo.mapper.AuthorMapper;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.entity.Author;
import ru.valkeru.libdemo.model.request.author.AuthorFilter;
import ru.valkeru.libdemo.model.transport.AuthorListDto;
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
    public Page<AuthorListDto> listAllAuthors(AuthorFilter filter, Pageable pageable) {
        Page<AuthorDto> authorDtos = authorSearchProvider.listAllAuthors(filter, pageable);

        return authorDtos.map(dto -> new AuthorListDto(
            dto.getId(),
            "%s %s %s".formatted(dto.getFirstName(), dto.getMiddleName(), dto.getLastName())
        ));
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
