package ru.valkeru.libdemo.repository.elasticsearch.impl;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import ru.valkeru.libdemo.model.document.AuthorDocument;
import ru.valkeru.libdemo.model.document.QAuthorDocument;
import ru.valkeru.libdemo.model.request.author.AuthorFilter;
import ru.valkeru.libdemo.repository.elasticsearch.AuthorElasticsearchCustomRepository;
import ru.valkeru.libdemo.util.QueryUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Repository
public class AuthorElasticsearchRepositoryImpl implements AuthorElasticsearchCustomRepository {

    private final ElasticsearchTemplate elasticsearchTemplate;

    public AuthorElasticsearchRepositoryImpl(ElasticsearchTemplate elasticsearchTemplate) {
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    @Override
    public Page<AuthorDocument> listAllAuthors(AuthorFilter filter, Pageable pageable) {
        SearchHits<AuthorDocument> searchHits = elasticsearchTemplate.search(buildElasticsearchQuery(filter, pageable), AuthorDocument.class);
        Page<AuthorDocument> authors = getPage(searchHits, pageable);

        Assert.notNull(authors, "Search result is null");

        return authors;
    }

    @Override
    public AuthorDocument getAuthorById(UUID id) {
        return elasticsearchTemplate.get(String.valueOf(id), AuthorDocument.class);
    }


    private NativeQuery buildElasticsearchQuery(AuthorFilter filter, Pageable pageable) {
        NativeQueryBuilder queryBuilder = NativeQuery.builder().withPageable(pageable);

        List<Query> queries = applyFilter(filter);


        return queryBuilder.withQuery(q -> q.bool(b -> b.must(queries)))
                .build();
    }

    private List<Query> applyFilter(AuthorFilter filter) {
        List<Query> queries = new ArrayList<>();

        QueryUtil.applyLikeCondition(filter.getFirstName(), queries, QAuthorDocument.authorDocument.firstName);
        QueryUtil.applyLikeCondition(filter.getMiddleName(), queries, QAuthorDocument.authorDocument.middleName);
        QueryUtil.applyLikeCondition(filter.getLastName(), queries, QAuthorDocument.authorDocument.lastName);

        return queries;
    }

    private Page<AuthorDocument> getPage(SearchHits<AuthorDocument> searchHits, Pageable pageable) {
        List<AuthorDocument> documents = searchHits.stream()
                .map(SearchHit::getContent)
                .toList();

        return new PageImpl<>(documents, pageable, searchHits.getTotalHits());
    }
}
