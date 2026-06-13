package ru.valkeru.libdemo.infrastructure.elasticsearch;

public interface ElasticsearchService {

    void createOrUpdateElasticsearchIndex(Class<?> clazz);
}
