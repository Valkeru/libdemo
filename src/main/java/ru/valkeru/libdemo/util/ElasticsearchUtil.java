package ru.valkeru.libdemo.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.IndexOperations;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ElasticsearchUtil implements ApplicationContextAware {

    private static ElasticsearchTemplate elasticsearchTemplate;

    private static void initDependencies(ApplicationContext applicationContext) {
        elasticsearchTemplate = applicationContext.getBean(ElasticsearchTemplate.class);
    }

    @Override
    public void setApplicationContext(@NonNull ApplicationContext applicationContext) throws BeansException {
        initDependencies(applicationContext);
    }

    public static void createOrUpdateElasticsearchIndex(Class<?> clazz) {
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
