package ru.valkeru.libdemo.repository.qdsl.impl;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import com.querydsl.core.types.Expression;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder;
import org.springframework.data.elasticsearch.core.SearchHitSupport;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import ru.valkeru.libdemo.mapper.AuthorMapper;
import ru.valkeru.libdemo.model.document.AuthorDocument;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.entity.Author;
import ru.valkeru.libdemo.model.entity.QAuthor;
import ru.valkeru.libdemo.model.request.author.AuthorFilter;
import ru.valkeru.libdemo.repository.qdsl.base.AuthorDslRepository;
import ru.valkeru.libdemo.util.QueryUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthorRepositoryImpl extends QuerydslRepositorySupport implements AuthorDslRepository {

    ElasticsearchTemplate elasticsearchTemplate;
    EntityManager entityManager;
    AuthorMapper authorMapper;

    public AuthorRepositoryImpl(ElasticsearchTemplate elasticsearchTemplate, EntityManager entityManager,
                                AuthorMapper authorMapper) {
        super(Author.class);

        this.elasticsearchTemplate = elasticsearchTemplate;
        this.entityManager = entityManager;
        this.authorMapper = authorMapper;
    }

    @Override
    public List<AuthorDto> listAllAuthors(AuthorFilter filter, Pageable pageable) {
        try {
            SearchHits<AuthorDocument> searchHits = elasticsearchTemplate.search(buildElasticsearchQuery(filter, pageable), AuthorDocument.class);
            Page<AuthorDocument> authors = (Page<AuthorDocument>) SearchHitSupport.unwrapSearchHits(SearchHitSupport.searchPageFor(searchHits, pageable));
            Assert.notNull(authors, "Search result is null");

            return authors.getContent().stream()
                    .map(authorMapper::toDto)
                    .toList();
        } catch (Exception e) {
            log.error("Elasticsearch Authors list search failed: {}", e.getMessage(), e);

            JPAQuery<Author> authorJPAQuery = buildJpaQuery(new QAuthor(QAuthor.author), filter);

            return authorJPAQuery.fetch().stream()
                    .map(authorMapper::toDto)
                    .toList();
        }
    }

    @Override
    public AuthorDto getAuthorById(Long id) {
        try {
            AuthorDocument authorDocument = elasticsearchTemplate.get(String.valueOf(id), AuthorDocument.class);

            return authorMapper.toDto(authorDocument);
        } catch (Exception e) {
            log.error("Elasticsearch Author search failed: {}", e.getMessage(), e);

            return authorMapper.toDto(entityManager.find(Author.class, id, Map.of("id", id)));
        }
    }

    private NativeQuery buildElasticsearchQuery(AuthorFilter filter, Pageable pageable) {
        NativeQueryBuilder queryBuilder = NativeQuery.builder().withPageable(pageable);

        List<Query> queries = new ArrayList<>();
        QueryUtil.applyLikeCondition(filter.getFirstName(), queries, QAuthor.author.firstName);
        QueryUtil.applyLikeCondition(filter.getMiddleName(), queries, QAuthor.author.middleName);
        QueryUtil.applyLikeCondition(filter.getLastName(), queries, QAuthor.author.lastName);

        return queryBuilder.withQuery(q -> q.bool(b -> b.must(queries)))
                .build();
    }

    private JPAQuery<Author> buildJpaQuery(Expression<Author> expression, AuthorFilter filter) {
        JPAQuery<Author> query = new JPAQuery<Author>(entityManager)
                .select(expression)
                .from(QAuthor.author);

        applyFilter(filter, query);

        return query;
    }

    private void applyFilter(AuthorFilter filter, JPAQuery<Author> query) {
        QueryUtil.applyLikeCondition(filter.getFirstName(), query, QAuthor.author.firstName);
        QueryUtil.applyLikeCondition(filter.getMiddleName(), query, QAuthor.author.middleName);
        QueryUtil.applyLikeCondition(filter.getLastName(), query, QAuthor.author.lastName);
    }
}
