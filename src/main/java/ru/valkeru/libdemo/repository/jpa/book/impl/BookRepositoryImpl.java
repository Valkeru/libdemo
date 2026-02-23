package ru.valkeru.libdemo.repository.jpa.book.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLSubQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import ru.valkeru.libdemo.model.entity.Book;
import ru.valkeru.libdemo.model.entity.QAuthor;
import ru.valkeru.libdemo.model.entity.QBook;
import ru.valkeru.libdemo.model.request.book.BookFilter;
import ru.valkeru.libdemo.repository.jpa.BaseDslRepository;
import ru.valkeru.libdemo.repository.jpa.book.BookDslRepository;
import ru.valkeru.libdemo.repository.jpa.projection.BookShortProjection;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Repository
public class BookRepositoryImpl extends BaseDslRepository<Book> implements BookDslRepository {

    private static final QBook qBook = QBook.book;
    private static final QAuthor qAuthor = QAuthor.author;

    public BookRepositoryImpl(EntityManager em, JPAQueryFactory queryFactory) {
        super(Book.class, em, queryFactory);
    }

    @Override
    public Page<BookShortProjection> findAllBooks(BookFilter filter, Pageable pageable) {
        BooleanBuilder whereClause = new BooleanBuilder();
        applyIContainsCondition(filter.getName(), whereClause, qBook.name);
        applyIContainsCondition(filter.getIsbn(), whereClause, qBook.isbn);

        JPAQuery<?> baseQuery = getBaseQuery(filter, whereClause);

        long count = getCount(baseQuery);
        if (count == 0) {
            return Page.empty(pageable);
        }

        List<UUID> ids = selectIds(baseQuery, pageable);

        Map<UUID, BookShortProjection> transformed = queryFactory
                .from(qBook)
                .leftJoin(qBook.authors, qAuthor)
                .where(qBook.id.in(ids))
                .transform(GroupBy.groupBy(qBook.id).as(Projections.constructor(
                        BookShortProjection.class,
                        qBook.id,
                        qBook.name,
                        qBook.isbn,
                        GroupBy.list(getFullAuthorNameExpression())
                )));

        List<BookShortProjection> result = ids.stream()
                .map(transformed::get)
                .filter(Objects::nonNull) // На всякий случай
                .toList();

        return PageableExecutionUtils.getPage(
                result,
                pageable,
                () -> count
        );
    }

    private JPAQuery<?> getBaseQuery(BookFilter filter, BooleanBuilder whereClause) {
        JPAQuery<?> baseQuery = queryFactory.from(qBook);

        String authorName = filter.getAuthorName();
        if (StringUtils.isNotBlank(authorName)) {
            JPQLSubQuery<Integer> from = JPAExpressions.selectOne()
                    .from(qAuthor)
                    .where(qAuthor.in(qBook.authors));

            applyIContainsCondition(authorName, from, getFullAuthorNameExpression());

            baseQuery.where(from.exists());
        }

        return baseQuery.where(whereClause);
    }

    private static long getCount(JPAQuery<?> baseQuery) {
        JPAQuery<Long> countQuery = baseQuery.clone().select(qBook.id.count());

        return Optional.ofNullable(countQuery.fetchOne()).orElse(0L);
    }

    private List<UUID> selectIds(JPAQuery<?> baseQuery, Pageable pageable) {
        JPAQuery<?> baseIdQuery = baseQuery.clone();
        querydsl.applyPagination(pageable, baseIdQuery);

        return baseIdQuery.select(qBook.id).fetch();
    }

    private StringExpression getFullAuthorNameExpression() {
        return qAuthor.firstName
                .concat(StringUtils.SPACE)
                .concat(qAuthor.middleName.coalesce(StringUtils.EMPTY))
                .concat(StringUtils.SPACE)
                .concat(qAuthor.lastName);
    }
}
