package ru.valkeru.libdemo.repository.elasticsearch.impl;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import ru.valkeru.libdemo.mapper.AuthorMapper;
import ru.valkeru.libdemo.model.document.AuthorDocument;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.entity.QAuthor;
import ru.valkeru.libdemo.model.request.author.AuthorFilter;
import ru.valkeru.libdemo.repository.elasticsearch.base.AuthorElasticsearchCustomRepository;
import ru.valkeru.libdemo.util.QueryUtil;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class AuthorElasticsearchRepositoryImpl implements AuthorElasticsearchCustomRepository {

    private final ElasticsearchTemplate elasticsearchTemplate;
    private final AuthorMapper authorMapper;

    public AuthorElasticsearchRepositoryImpl(ElasticsearchTemplate elasticsearchTemplate, AuthorMapper authorMapper) {
        this.elasticsearchTemplate = elasticsearchTemplate;
        this.authorMapper = authorMapper;
    }

    @Override
    public Page<AuthorDto> listAllAuthors(AuthorFilter filter, Pageable pageable) {
        SearchHits<AuthorDocument> searchHits = elasticsearchTemplate.search(buildElasticsearchQuery(filter, pageable), AuthorDocument.class);
        Page<AuthorDocument> authors = (Page<AuthorDocument>) SearchHitSupport.unwrapSearchHits(SearchHitSupport.searchPageFor(searchHits, pageable));

        Assert.notNull(authors, "Search result is null");

        return authors.map(authorMapper::toDto);
    }

    @Override
    public AuthorDto getAuthorById(Long id) {
        AuthorDocument authorDocument = elasticsearchTemplate.get(String.valueOf(id), AuthorDocument.class);

        return authorMapper.toDto(authorDocument);
    }


    private NativeQuery buildElasticsearchQuery(AuthorFilter filter, Pageable pageable) {
        NativeQueryBuilder queryBuilder = NativeQuery.builder().withPageable(pageable);

        List<Query> queries = applyFilter(filter);


        return queryBuilder.withQuery(q -> q.bool(b -> b.must(queries)))
                .build();
    }

    private List<Query> applyFilter(AuthorFilter filter) {
        List<Query> queries = new ArrayList<>();

        QueryUtil.applyLikeCondition(filter.getFirstName(), queries, QAuthor.author.firstName);
        QueryUtil.applyLikeCondition(filter.getMiddleName(), queries, QAuthor.author.middleName);
        QueryUtil.applyLikeCondition(filter.getLastName(), queries, QAuthor.author.lastName);

        return queries;
    }
}
