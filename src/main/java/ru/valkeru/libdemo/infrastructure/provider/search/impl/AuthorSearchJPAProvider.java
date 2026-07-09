package ru.valkeru.libdemo.infrastructure.provider.search.impl;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.valkeru.libdemo.mapper.AuthorMapper;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.domain.entity.Author;
import ru.valkeru.libdemo.domain.repository.jpa.author.AuthorRepository;
import ru.valkeru.libdemo.infrastructure.provider.search.AuthorSearchProvider;

@Service
@Slf4j
public class AuthorSearchJPAProvider implements AuthorSearchProvider {

    private final AuthorRepository repository;
    private final AuthorMapper mapper;

    public AuthorSearchJPAProvider(AuthorRepository repository, AuthorMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @PostConstruct
    void logComponentStarted() {
        log.info("JPA search provider started");
    }

    @Override
    public Page<AuthorDto> listAllAuthors(String[] tokens, Pageable pageable) {
        Page<Author> authors = repository.listAllAuthors(tokens, pageable);

        return authors.map(mapper::toDto);
    }
}
