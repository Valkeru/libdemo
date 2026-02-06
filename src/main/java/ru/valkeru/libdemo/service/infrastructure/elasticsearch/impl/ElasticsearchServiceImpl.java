package ru.valkeru.libdemo.service.infrastructure.elasticsearch.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.stereotype.Service;
import ru.valkeru.libdemo.service.infrastructure.elasticsearch.ElasticsearchService;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.system.elasticsearch-enabled", havingValue = "true")
public class ElasticsearchServiceImpl implements ElasticsearchService {

    private final ElasticsearchTemplate elasticsearchTemplate;

    @Override
    public void createOrUpdateElasticsearchIndex(Class<?> clazz) {
        IndexOperations indexOperations = elasticsearchTemplate.indexOps(clazz);
        String indexName = indexOperations.getIndexCoordinates().getIndexName();
        log.info("Create or update elasticsearch index {} requested", indexName);

        if (indexOperations.exists()) {
            log.info("Index {} exists, update mapping only", indexOperations);

            indexOperations.putMapping();
        }

        indexOperations.create(indexOperations.createSettings(), indexOperations.createMapping());
        log.info("Created index {}", indexName);
    }
}
