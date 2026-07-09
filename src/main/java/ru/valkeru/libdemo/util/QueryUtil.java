package ru.valkeru.libdemo.util;

import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.WildcardQuery;
import com.querydsl.core.types.dsl.StringPath;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.experimental.UtilityClass;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class QueryUtil {

    public <T> void applyLikeCondition(String condition, JPAQuery<T> query, StringPath path) {
        if (!StringUtils.hasLength(condition)) {
            return;
        }

        query.where(path.containsIgnoreCase(condition));
    }

    public void applyWildcardCondition(String condition, List<Query> queries, String ...paths) {
        if (!StringUtils.hasLength(condition)) {
            return;
        }

        List<Query> wildcardQueries = new ArrayList<>();

        for (String path : paths) {
            WildcardQuery wildcard = new WildcardQuery.Builder()
                .wildcard("*%s*".formatted(condition))
                .field(path)
                .build();

            wildcardQueries.add(wildcard._toQuery());
        }

        queries.add(
            new BoolQuery.Builder()
                .should(wildcardQueries)
                .minimumShouldMatch("1")
                .build()
                ._toQuery()
        );
    }
}
