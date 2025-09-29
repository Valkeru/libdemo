package ru.valkeru.libdemo.repository.elasticsearch.base;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import ru.valkeru.libdemo.model.document.AuthorDocument;

public interface AuthorElasticsearchRepository
        extends ElasticsearchRepository<AuthorDocument, Long>, AuthorElasticsearchCustomRepository {
}
