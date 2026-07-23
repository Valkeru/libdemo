package ru.valkeru.libdemo.application.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.valkeru.libdemo.exception.impl.AuthorNotFoundException;
import ru.valkeru.libdemo.mapper.AuthorMapper;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.persistence.entity.Author;
import ru.valkeru.libdemo.model.request.author.AuthorFilter;
import ru.valkeru.libdemo.model.transport.AuthorListDto;
import ru.valkeru.libdemo.persistence.service.AuthorService;
import ru.valkeru.libdemo.application.AuthorApplicationService;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorApplicationServiceImpl implements AuthorApplicationService {

    private final AuthorService authorService;
    private final AuthorMapper authorMapper;

    @Override
    @Transactional
    public UUID createAuthor(AuthorDto authorDto) {
        Author author = authorService.createAuthor(authorDto);

        return author.getId();
    }

    @Override
    @Transactional
    public void updateAuthor(AuthorDto dto) {
        UUID id = dto.getId();

        Author author = authorService.findById(id)
            .orElseThrow(() -> AuthorNotFoundException.authorNotFound(id));
        authorService.update(dto, author);
    }

    @Override
    public Page<AuthorListDto> listAllAuthors(AuthorFilter filter, Pageable pageable) {
        Page<AuthorDto> authorDtos = authorService.listAllAuthors(filter, pageable);

        return authorDtos.map(dto -> new AuthorListDto(
            dto.getId(),
            "%s %s %s".formatted(dto.getFirstName(), dto.getMiddleName(), dto.getLastName())
        ));
    }

    @Override
    @Cacheable("author")
    public AuthorDto getAuthorById(UUID id) {
        Author author = authorService.findById(id)
            .orElseThrow(() -> AuthorNotFoundException.authorNotFound(id));

        return authorMapper.toDto(author);
    }

    @Transactional
    @Override
    public void deleteAuthor(UUID id) {
        authorService.deleteAuthorById(id);
    }

    @Override
    public void reindexAuthors() {
        authorService.reindexAuthors();
    }
}
