package ru.valkeru.libdemo.infrastructure.elasticsearch.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.BulkRequest;
import co.elastic.clients.elasticsearch.indices.UpdateAliasesRequest;
import co.elastic.clients.elasticsearch.indices.update_aliases.Action;
import co.elastic.clients.transport.endpoints.BooleanResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.index.Settings;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.stereotype.Service;
import ru.valkeru.libdemo.infrastructure.elasticsearch.ElasticsearchService;

import java.io.IOException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(name = "app.system.elasticsearch-enabled", havingValue = "true")
public class ElasticsearchServiceImpl implements ElasticsearchService {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final ElasticsearchClient elasticsearchClient;

    @Override
    public String createIndex(Class<?> clazz) throws IOException {
        IndexOperations aliasOperations = elasticsearchTemplate.indexOps(clazz);
        IndexCoordinates aliasCoordinates = aliasOperations.getIndexCoordinates();

        String baseName = aliasCoordinates.getIndexName();
        String suffix = getIndexDateTimeSuffix();

        String fullName = baseName + "_" + suffix;

        BooleanResponse existsResponse = elasticsearchClient.indices().exists(builder -> builder.index(fullName));
        if (existsResponse.value()) {
            log.warn("Index '{}' already exists, skip next operations", fullName);

            return fullName;
        }

        IndexCoordinates realIndexCoordinates = IndexCoordinates.of(fullName);
        IndexOperations realIndexOperations = elasticsearchTemplate.indexOps(realIndexCoordinates);

        Settings settings = aliasOperations.createSettings();
        Document mapping = aliasOperations.createMapping();
        realIndexOperations.create(settings, mapping);

        log.info("Index '{}' created", fullName);

        return fullName;
    }

    @Override
    public void reassignAlias(Class<?> clazz, String newIndexName) throws IOException {
        IndexOperations aliasOperations = elasticsearchTemplate.indexOps(clazz);
        String aliasName = aliasOperations.getIndexCoordinates().getIndexName();

        UpdateAliasesRequest.Builder updateBuilder = new UpdateAliasesRequest.Builder();

        Action.Builder deleteBuilder = new Action.Builder();
        deleteBuilder.remove(builder -> builder.alias(aliasName)
            .index("%s_*".formatted(aliasName))
            .mustExist(false)
        );

        updateBuilder.actions(deleteBuilder.build());

        Action.Builder createBuilder = new Action.Builder();
        createBuilder.add(builder -> builder.alias(aliasName).index(newIndexName));
        updateBuilder.actions(createBuilder.build());

        elasticsearchClient.indices().updateAliases(updateBuilder.build());
    }

    @Override
    public <T> void saveAll(Collection<T> documents, String indexName) throws IOException {
        if (CollectionUtils.isEmpty(documents)) {
            return;
        }

        BulkRequest.Builder bb = new BulkRequest.Builder();
        for (T document : documents) {
            bb.operations(op -> op
                .index(idx -> idx
                    .index(indexName)
                    .document(document)
                )
            );
        }

        elasticsearchClient.bulk(bb.build());
    }

    @Override
    public void refreshIndex(String indexName) throws IOException {
        elasticsearchClient.indices().refresh(builder -> builder.index(indexName));
    }

    private String getIndexDateTimeSuffix() {
        Instant now = Instant.now();
        LocalDateTime ldt = LocalDateTime.ofInstant(now, ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyddMM't'HH-mm-ss");

        return formatter.format(ldt);
    }
}
