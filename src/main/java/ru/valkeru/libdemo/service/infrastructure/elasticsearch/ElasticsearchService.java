package ru.valkeru.libdemo.service.infrastructure.elasticsearch;

public interface ElasticsearchService {

    void createOrUpdateElasticsearchIndex(Class<?> clazz);
}
