package ru.valkeru.libdemo.persistence.provider.search.impl;

import jakarta.annotation.PostConstruct;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StopWatch;
import ru.valkeru.libdemo.constants.CommonConstants;
import ru.valkeru.libdemo.infrastructure.elasticsearch.ElasticsearchService;
import ru.valkeru.libdemo.persistence.entity.Author;
import ru.valkeru.libdemo.persistence.provider.search.AuthorSearchProvider;
import ru.valkeru.libdemo.persistence.provider.search.IndexingSupport;
import ru.valkeru.libdemo.mapper.AuthorMapper;
import ru.valkeru.libdemo.persistence.document.AuthorDocument;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.persistence.repository.elasticsearch.AuthorElasticsearchRepository;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@Primary
@ConditionalOnProperty(name = "app.system.elasticsearch-enabled", havingValue = "true")
public class AuthorSearchElasticsearchProvider implements AuthorSearchProvider, IndexingSupport {

    private static final int INDEXING_SLICE_SIZE = 1000;

    private final ElasticsearchService elasticsearchService;
    private final AuthorSearchJPAProvider databaseSearchProvider;
    private final AuthorElasticsearchRepository repository;
    private final AuthorMapper mapper;

    public AuthorSearchElasticsearchProvider(AuthorElasticsearchRepository repository,
                                             AuthorSearchJPAProvider databaseSearchProvider,
                                             AuthorMapper mapper, ElasticsearchService elasticsearchService) {
        this.repository = repository;
        this.elasticsearchService = elasticsearchService;
        this.databaseSearchProvider = databaseSearchProvider;
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

            return databaseSearchProvider.listAllAuthors(tokens, pageable);
        }
    }

    @Override
    @SneakyThrows
    public void reindex() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();

        log.info("Authors reindexing started");

        String realIndexName = elasticsearchService.createIndex(AuthorDocument.class);

        updateIndex(realIndexName);

        elasticsearchService.refreshIndex(realIndexName);
        elasticsearchService.reassignAlias(AuthorDocument.class, realIndexName);

        stopWatch.stop();
        log.info("Authors reindexing finished: {}", stopWatch);
    }

    /**
     * Reads authors entities from the database and indexes them into the specified Elasticsearch index
     * @param indexName Real Elasticsearch index name
     */
    private void updateIndex(final String indexName) throws IOException {
        Instant createdAtStart = Instant.EPOCH;
        UUID idStart = UUID.fromString(CommonConstants.UUID_START_VALUE);

        while (true) {
            List<Author> slice = databaseSearchProvider.getAuthorsForIndexing(createdAtStart, idStart, INDEXING_SLICE_SIZE);

            if (slice.isEmpty()) {
                break;
            }

            List<AuthorDocument> documents = slice.stream().map(mapper::toDocument).toList();

            elasticsearchService.saveAll(documents, indexName);

            Author last = slice.getLast();
            createdAtStart = last.getCreatedAt();
            idStart = last.getId();
        }
    }
}
