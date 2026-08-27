package ru.valkeru.libdemo.domain.repository.jpa.author.qdsl.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Repository;
import ru.valkeru.libdemo.domain.entity.Author;
import ru.valkeru.libdemo.domain.entity.QAuthor;
import ru.valkeru.libdemo.domain.repository.jpa.BaseDslRepository;
import ru.valkeru.libdemo.domain.repository.jpa.author.qdsl.AuthorDslRepository;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

@Repository
public class AuthorRepositoryImpl extends BaseDslRepository<Author> implements AuthorDslRepository {

    private static final QAuthor qAuthor = QAuthor.author;

    private final JdbcClient jdbcClient;

    public AuthorRepositoryImpl(EntityManager em, JPAQueryFactory queryFactory, JdbcClient jdbcClient) {
        super(Author.class, em, queryFactory);
        this.jdbcClient = jdbcClient;
    }

    @Override
    public Page<Author> listAllAuthors(String[] tokens, Pageable pageable) {
        BooleanBuilder whereClause = new BooleanBuilder();

        for (String token : tokens) {
            BooleanBuilder tokenBlock = new BooleanBuilder();
            applyOrIStartsWith(token, tokenBlock, qAuthor.firstName, qAuthor.middleName, qAuthor.lastName);

            whereClause.and(tokenBlock);
        }

        JPAQuery<?> baseQuery = queryFactory.from(qAuthor).where(whereClause);
        JPAQuery<UUID> idQuery = baseQuery.clone().select(qAuthor.id);
        querydsl.applyPagination(pageable, idQuery);
        List<UUID> ids = idQuery.fetch();

        if (CollectionUtils.isEmpty(ids)) {
            return Page.empty(pageable);
        }

        JPAQuery<Author> mainQuery = queryFactory
            .select(qAuthor)
            .from(qAuthor)
            .where(qAuthor.id.in(ids));
        querydsl.applySorting(pageable.getSort(), mainQuery);

        return PageableExecutionUtils.getPage(
            mainQuery.fetch(),
            pageable,
            () -> getCount(baseQuery)
        );
    }

    @Override
    public List<Author> getAuthorSlice(Instant createdAt, UUID id, int limit) {
        LocalDateTime ldt = LocalDateTime.ofInstant(createdAt, ZoneOffset.UTC);

        return jdbcClient.sql("""
                    SELECT * FROM library.author a
                    WHERE (a.created_at, a.id) > (:createdAt, :id)
                    ORDER BY a.created_at, a.id
                    LIMIT :limit
                """)
            .param("createdAt", ldt)
            .param("id", id)
            .param("limit", limit)
            .query(Author.class)
            .list();
    }
}
