package ru.valkeru.libdemo.domain.repository.elasticsearch;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import ru.valkeru.libdemo.domain.document.AuthorDocument;

public interface AuthorElasticsearchRepository
        extends ElasticsearchRepository<AuthorDocument, Long>, AuthorElasticsearchCustomRepository {
}
