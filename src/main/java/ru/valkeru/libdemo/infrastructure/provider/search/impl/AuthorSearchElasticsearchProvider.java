package ru.valkeru.libdemo.infrastructure.provider.search.impl;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.valkeru.libdemo.infrastructure.elasticsearch.ElasticsearchService;
import ru.valkeru.libdemo.infrastructure.provider.search.AuthorSearchProvider;
import ru.valkeru.libdemo.mapper.AuthorMapper;
import ru.valkeru.libdemo.domain.document.AuthorDocument;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.domain.repository.elasticsearch.AuthorElasticsearchRepository;

import java.util.List;

@Slf4j
@Service
@Primary
@ConditionalOnProperty(name = "app.system.elasticsearch-enabled", havingValue = "true")
public class AuthorSearchElasticsearchProvider implements AuthorSearchProvider {

    private final ElasticsearchService elasticsearchService;
    private final AuthorSearchJPAProvider fallbackProvider;
    private final AuthorElasticsearchRepository repository;
    private final AuthorMapper mapper;

    public AuthorSearchElasticsearchProvider(AuthorElasticsearchRepository repository,
                                             AuthorSearchJPAProvider fallbackProvider,
                                             AuthorMapper mapper, ElasticsearchService elasticsearchService) {
        this.repository = repository;
        this.elasticsearchService = elasticsearchService;
        this.fallbackProvider = fallbackProvider;
        this.mapper = mapper;
    }

    @PostConstruct
    void logComponentStarted() {
        log.info("Elasticsearch search provider started");
    }

    @Override
    public Page<AuthorDto> listAllAuthors(String[] tokens, Pageable pageable) {
        try {
            Page<AuthorDocument> documents = repository.listAllAuthors(tokens, pageable);

            return documents.map(mapper::toDto);
        } catch (Exception e) {
            log.error("Failed to search authors using elasticsearch, falling back to database", e);

            return fallbackProvider.listAllAuthors(tokens, pageable);
        }
    }

    @Override
    public void reindex() {
        elasticsearchService.createOrUpdateElasticsearchIndex(AuthorDocument.class);

        List<AuthorDocument> authorDocuments = fallbackProvider.listAllAuthors(new String[0], Pageable.unpaged())
            .map(mapper::toDocument)
            .toList();

        repository.deleteAll();
        repository.saveAll(authorDocuments);
    }
}
