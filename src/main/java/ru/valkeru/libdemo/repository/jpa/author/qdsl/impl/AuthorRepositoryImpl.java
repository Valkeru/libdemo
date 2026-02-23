package ru.valkeru.libdemo.repository.jpa.author.qdsl.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import ru.valkeru.libdemo.model.entity.Author;
import ru.valkeru.libdemo.model.entity.QAuthor;
import ru.valkeru.libdemo.model.request.author.AuthorFilter;
import ru.valkeru.libdemo.repository.jpa.BaseDslRepository;
import ru.valkeru.libdemo.repository.jpa.author.qdsl.AuthorDslRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class AuthorRepositoryImpl extends BaseDslRepository<Author> implements AuthorDslRepository {

    private static final QAuthor qAuthor = QAuthor.author;

    public AuthorRepositoryImpl(EntityManager em, JPAQueryFactory queryFactory) {
        super(Author.class, em, queryFactory);
    }

    @Override
    public Page<Author> listAllAuthors(AuthorFilter filter, Pageable pageable) {
        BooleanBuilder whereClause = new BooleanBuilder();
        applyIContainsCondition(filter.getFirstName(), whereClause, qAuthor.firstName);
        applyIContainsCondition(filter.getMiddleName(), whereClause, qAuthor.middleName);
        applyIContainsCondition(filter.getLastName(), whereClause, qAuthor.lastName);

        JPAQuery<?> baseQuery = queryFactory.from(qAuthor).where(whereClause);

        JPAQuery<Long> countQuery = baseQuery.clone().select(qAuthor.count());
        long count = Optional.ofNullable(countQuery.fetchOne()).orElse(0L);
        if (count == 0) {
            return Page.empty(pageable);
        }

        JPAQuery<UUID> idQuery = baseQuery.clone().select(qAuthor.id);
        querydsl.applyPagination(pageable, idQuery);
        List<UUID> ids = idQuery.fetch();

        JPAQuery<Author> mainQuery = queryFactory
                .select(qAuthor)
                .from(qAuthor)
                .where(qAuthor.id.in(ids));
        querydsl.applySorting(pageable.getSort(), mainQuery);

        return PageableExecutionUtils.getPage(
                mainQuery.fetch(),
                pageable,
                () -> count
        );
    }
}
