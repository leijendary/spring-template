package com.leijendary.spring.boot.template.util;

import static com.leijendary.spring.boot.template.util.RequestContext.getLanguage;
import static java.util.Arrays.stream;
import static org.elasticsearch.common.unit.Fuzziness.AUTO;
import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;
import static org.elasticsearch.index.query.QueryBuilders.termQuery;
import static org.elasticsearch.index.query.QueryBuilders.wildcardQuery;
import static org.elasticsearch.search.sort.SortBuilders.fieldSort;
import static org.elasticsearch.search.sort.SortOrder.fromString;

import java.util.ArrayList;
import java.util.List;

import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.index.query.WildcardQueryBuilder;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.NestedSortBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class SearchUtil {

    public static BoolQueryBuilder match(final String query, final String... names) {
        final var boolQuery = boolQuery();

        stream(names).forEach(name -> {
            final var matchQuery = match(query, name);

            boolQuery.should(matchQuery);
        });

        return boolQuery;
    }

    public static MatchQueryBuilder match(final String query, final String name) {
        return matchQuery(name, query).fuzziness(AUTO);
    }

    public static BoolQueryBuilder wildcard(final String query, final String... names) {
        final var boolQuery = boolQuery();

        stream(names).forEach(name -> {
            final var wildcardQuery = wildcard(query, name);

            boolQuery.should(wildcardQuery);
        });

        return boolQuery;
    }

    public static WildcardQueryBuilder wildcard(final String query, final String name) {
        return wildcardQuery(name, "*" + query + "*").caseInsensitive(true);
    }

    public static List<SortBuilder<?>> sortBuilders(final Pageable pageable) {
        final var sortBuilders = new ArrayList<SortBuilder<?>>();

        pageable.getSort().stream().forEach(order -> {
            final var field = order.getProperty();
            final var direction = order.getDirection();

            if (field.startsWith("translations.")) {
                final var nestedSort = nestedTranslations(field, direction);

                sortBuilders.add(nestedSort);
            } else {
                final var fieldSort = field(field, direction);

                sortBuilders.add(fieldSort);
            }
        });

        return sortBuilders;
    }

    public static FieldSortBuilder nestedTranslations(final String field, final Sort.Direction direction) {
        return nested("translations", field, direction);
    }

    public static FieldSortBuilder nested(final String path, final String field, final Sort.Direction direction) {
        final var nestedSort = new NestedSortBuilder(path)
                .setFilter(languageQuery());

        return field(field, direction).setNestedSort(nestedSort);
    }

    public static FieldSortBuilder field(final String field, final Sort.Direction direction) {
        final var sortOrder = fromString(direction.toString());

        return fieldSort(field).order(sortOrder);
    }

    public static TermQueryBuilder languageQuery() {
        final var language = getLanguage();

        return termQuery("translations.language", language);
    }
}
