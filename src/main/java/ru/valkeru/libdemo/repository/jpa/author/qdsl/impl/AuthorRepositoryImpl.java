package ru.valkeru.libdemo.repository.jpa.author.qdsl.impl;

import com.querydsl.core.types.Expression;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;
import ru.valkeru.libdemo.model.entity.Author;
import ru.valkeru.libdemo.model.entity.QAuthor;
import ru.valkeru.libdemo.model.request.author.AuthorFilter;
import ru.valkeru.libdemo.repository.jpa.author.qdsl.AuthorDslRepository;
import ru.valkeru.libdemo.util.QueryUtil;

@Slf4j
@Repository
public class AuthorRepositoryImpl extends QuerydslRepositorySupport implements AuthorDslRepository {

    private final EntityManager entityManager;

    public AuthorRepositoryImpl(EntityManager entityManager) {
        super(Author.class);

        this.entityManager = entityManager;
    }

    @Override
    public Page<Author> listAllAuthors(AuthorFilter filter, Pageable pageable) {
        Querydsl querydsl = getQuerydsl();

        Assert.notNull(querydsl, "Querydsl helper is not available");

        JPAQuery<Author> authorJPAQuery = buildJpaQuery(new QAuthor(QAuthor.author), filter);
        querydsl.applyPagination(pageable, authorJPAQuery);

        JPAQuery<Long> longJPAQuery = buildJpaQuery(QAuthor.author.count(), filter);

        return PageableExecutionUtils.getPage(
                authorJPAQuery.fetch(),
                pageable,
                longJPAQuery::fetchOne
        );
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
