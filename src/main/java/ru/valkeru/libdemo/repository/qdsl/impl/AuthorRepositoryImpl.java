package ru.valkeru.libdemo.repository.qdsl.impl;

import com.querydsl.core.types.Expression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.jpa.support.PageableUtils;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import ru.valkeru.libdemo.mapper.AuthorMapper;
import ru.valkeru.libdemo.model.dto.AuthorDto;
import ru.valkeru.libdemo.model.entity.Author;
import ru.valkeru.libdemo.model.entity.QAuthor;
import ru.valkeru.libdemo.model.request.author.AuthorFilter;
import ru.valkeru.libdemo.repository.qdsl.base.AuthorDslRepository;
import ru.valkeru.libdemo.util.QueryUtil;

import java.util.List;
import java.util.Map;

@Slf4j
@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthorRepositoryImpl extends QuerydslRepositorySupport implements AuthorDslRepository {

    EntityManager entityManager;
    AuthorMapper authorMapper;

    public AuthorRepositoryImpl(EntityManager entityManager, AuthorMapper authorMapper) {
        super(Author.class);

        this.entityManager = entityManager;
        this.authorMapper = authorMapper;
    }

    @Override
    public Page<AuthorDto> listAllAuthors(AuthorFilter filter, Pageable pageable) {
        Querydsl querydsl = getQuerydsl();

        Assert.notNull(querydsl, "Querydsl helper is not available");

        JPAQuery<Author> authorJPAQuery = buildJpaQuery(new QAuthor(QAuthor.author), filter);
        querydsl.applyPagination(pageable, authorJPAQuery);

        JPAQuery<Long> longJPAQuery = buildJpaQuery(QAuthor.author.count(), filter);

        Page<Author> authors = PageableExecutionUtils.getPage(
                authorJPAQuery.fetch(),
                pageable,
                longJPAQuery::fetchOne
        );

        return authors.map(authorMapper::toDto);
    }

    @Override
    public AuthorDto getAuthorById(Long id) {
        return authorMapper.toDto(entityManager.find(Author.class, id, Map.of("id", id)));
    }

    private <T> JPAQuery<T> buildJpaQuery(Expression<T> expression, AuthorFilter filter) {
        JPAQuery<T> query = new JPAQuery<Author>(entityManager)
                .select(expression)
                .from(QAuthor.author);

        applyFilter(filter, query);

        return query;
    }

    private <T> void applyFilter(AuthorFilter filter, JPAQuery<T> query) {
        QueryUtil.applyLikeCondition(filter.getFirstName(), query, QAuthor.author.firstName);
        QueryUtil.applyLikeCondition(filter.getMiddleName(), query, QAuthor.author.middleName);
        QueryUtil.applyLikeCondition(filter.getLastName(), query, QAuthor.author.lastName);
    }
}
