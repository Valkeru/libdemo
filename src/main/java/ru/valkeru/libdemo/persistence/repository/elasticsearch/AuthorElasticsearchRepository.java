package ru.valkeru.libdemo.persistence.repository.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import ru.valkeru.libdemo.persistence.document.AuthorDocument;

public interface AuthorElasticsearchRepository
        extends ElasticsearchRepository<AuthorDocument, Long>, AuthorElasticsearchCustomRepository {
}
