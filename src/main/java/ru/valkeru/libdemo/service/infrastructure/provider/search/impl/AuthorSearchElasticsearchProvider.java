package ru.valkeru.libdemo.service.infrastructure.provider.search.impl;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.valkeru.libdemo.mapper.AuthorMapper;
import ru.valkeru.libdemo.model.document.AuthorDocument;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.request.author.AuthorFilter;
import ru.valkeru.libdemo.repository.elasticsearch.AuthorElasticsearchRepository;
import ru.valkeru.libdemo.repository.jpa.author.AuthorRepository;

@Slf4j
@Service
@ConditionalOnProperty(name = "app.system.elasticsearch-enabled", havingValue = "true")
public class AuthorSearchElasticsearchProvider extends AuthorSearchJPAProvider {

    private final AuthorElasticsearchRepository elasticsearchRepository;

    public AuthorSearchElasticsearchProvider(AuthorElasticsearchRepository elasticsearchRepository,
                                             AuthorRepository fallbackRepository,
                                             AuthorMapper mapper) {
        super(fallbackRepository, mapper);
        this.elasticsearchRepository = elasticsearchRepository;
    }

    @PostConstruct
    @Override
    protected void logComponentStarted() {
        log.info("Elasticsearch search provider started");
    }

    @Override
    public Page<AuthorDto> listAllAuthors(AuthorFilter filter, Pageable pageable) {
        try {
            Page<AuthorDocument> documents = elasticsearchRepository.listAllAuthors(filter, pageable);

            return documents.map(mapper::toDto);
        } catch (Exception e) {
            log.error("Failed to search authors using elasticsearch, falling back to database", e);

            return super.listAllAuthors(filter, pageable);
        }
    }
}
