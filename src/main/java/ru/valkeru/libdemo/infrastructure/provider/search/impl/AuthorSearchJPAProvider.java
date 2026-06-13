package ru.valkeru.libdemo.infrastructure.provider.search.impl;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.valkeru.libdemo.mapper.AuthorMapper;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.entity.Author;
import ru.valkeru.libdemo.model.request.author.AuthorFilter;
import ru.valkeru.libdemo.repository.jpa.author.AuthorRepository;
import ru.valkeru.libdemo.infrastructure.provider.search.AuthorSearchProvider;

@Service
@Slf4j
@ConditionalOnProperty(name = "app.system.elasticsearch-enabled", havingValue = "false", matchIfMissing = true)
public class AuthorSearchJPAProvider implements AuthorSearchProvider {

    private final AuthorRepository repository;
    protected final AuthorMapper mapper;

    public AuthorSearchJPAProvider(AuthorRepository repository, AuthorMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @PostConstruct
    protected void logComponentStarted() {
        log.info("JPA search provider started");
    }

    @Override
    public Page<AuthorDto> listAllAuthors(AuthorFilter filter, Pageable pageable) {
        Page<Author> authors = repository.listAllAuthors(filter, pageable);

        return authors.map(mapper::toDto);
    }
}
