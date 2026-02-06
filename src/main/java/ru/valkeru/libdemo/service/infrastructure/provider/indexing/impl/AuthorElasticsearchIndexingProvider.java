package ru.valkeru.libdemo.service.infrastructure.provider.indexing.impl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.valkeru.libdemo.mapper.AuthorMapper;
import ru.valkeru.libdemo.model.document.AuthorDocument;
import ru.valkeru.libdemo.repository.elasticsearch.AuthorElasticsearchRepository;
import ru.valkeru.libdemo.repository.jpa.author.AuthorRepository;
import ru.valkeru.libdemo.service.infrastructure.provider.indexing.AuthorIndexingProvider;
import ru.valkeru.libdemo.util.ElasticsearchUtil;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.system.elasticsearch-enabled", havingValue = "true")
public class AuthorElasticsearchIndexingProvider implements AuthorIndexingProvider {

    private final AuthorElasticsearchRepository repository;
    private final AuthorRepository jpaRepository;
    private final AuthorMapper mapper;

    @PostConstruct
    protected void logComponentStarted() {
        log.info("Elasticsearch indexing provider started");
    }

    @Override
    public void reindexAuthors() {
        ElasticsearchUtil.createOrUpdateElasticsearchIndex(AuthorDocument.class);

        List<AuthorDocument> authorDocuments = jpaRepository.findAll(Pageable.unpaged())
                .map(mapper::toDocument)
                .toList();

        repository.deleteAll();
        repository.saveAll(authorDocuments);
    }
}
