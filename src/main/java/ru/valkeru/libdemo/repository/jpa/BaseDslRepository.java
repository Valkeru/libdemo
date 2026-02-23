package ru.valkeru.libdemo.repository.jpa;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.JPQLSubQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.repository.support.Querydsl;

public abstract class BaseDslRepository<T> {

    protected final Querydsl querydsl;
    protected final JPAQueryFactory queryFactory;

    protected BaseDslRepository(Class<T> domainClass, EntityManager em, JPAQueryFactory queryFactory) {
        this.querydsl = new Querydsl(em, new PathBuilderFactory().create(domainClass));
        this.queryFactory = queryFactory;
    }

    protected void applyIContainsCondition(String condition, JPAQuery<?> query, StringPath path) {
        if (StringUtils.isBlank(condition)) {
            return;
        }

        query.where(path.containsIgnoreCase(condition));
    }

    protected void applyIContainsCondition(String condition, BooleanBuilder whereClause, StringPath path) {
        if (StringUtils.isBlank(condition)) {
            return;
        }

        whereClause.and(path.containsIgnoreCase(condition));
    }

    protected void applyIContainsCondition(String condition, BooleanBuilder whereClause, StringExpression expression) {
        if (StringUtils.isBlank(condition)) {
            return;
        }

        whereClause.and(expression.containsIgnoreCase(condition));
    }

    protected void applyIContainsCondition(String condition, JPQLSubQuery<Integer> from, StringExpression expression) {
        if (StringUtils.isBlank(condition)) {
            return;
        }

        from.where(expression.containsIgnoreCase(condition));
    }
}
