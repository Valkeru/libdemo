package ru.valkeru.libdemo.util;

import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import java.lang.reflect.Field;
import java.util.List;

@UtilityClass
public class QueryUtil {

    public <T> void applyLikeCondition(String condition, JPAQuery<T> query, StringPath path) {
        if (StringUtils.hasLength(condition)) {
            query.where(path.containsIgnoreCase(condition));
        }
    }

    public void applyLikeCondition(String condition, List<Query> queries, StringPath path) {
        if (!StringUtils.hasLength(condition)) {
            return;
        }

        queries.add(
                new QueryStringQuery.Builder()
                        .fields(((Field) path.getAnnotatedElement()).getName())
                        .query("*%s*".formatted(condition))
                        .build()._toQuery()
        );
    }
}
