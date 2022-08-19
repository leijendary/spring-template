package com.leijendary.spring.template.core.util

import org.elasticsearch.common.unit.Fuzziness.AUTO
import org.elasticsearch.index.query.BoolQueryBuilder
import org.elasticsearch.index.query.MatchQueryBuilder
import org.elasticsearch.index.query.QueryBuilders.*
import org.elasticsearch.index.query.TermQueryBuilder
import org.elasticsearch.index.query.WildcardQueryBuilder
import org.elasticsearch.search.sort.FieldSortBuilder
import org.elasticsearch.search.sort.NestedSortBuilder
import org.elasticsearch.search.sort.SortBuilder
import org.elasticsearch.search.sort.SortBuilders.fieldSort
import org.elasticsearch.search.sort.SortOrder.fromString
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort

object SearchUtil {
    fun match(query: String?, vararg names: String): BoolQueryBuilder {
        val boolQuery = boolQuery()

        names.forEach {
            val matchQuery = match(query, it)
            boolQuery.should(matchQuery)
        }

        return boolQuery
    }

    fun match(query: String?, name: String): MatchQueryBuilder {
        return matchQuery(name, query).fuzziness(AUTO)
    }

    fun wildcard(query: String, vararg names: String): BoolQueryBuilder {
        val boolQuery = boolQuery()

        names.forEach {
            val wildcardQuery = wildcard(query, it)
            boolQuery.should(wildcardQuery)
        }

        return boolQuery
    }

    fun wildcard(query: String, name: String): WildcardQueryBuilder {
        return wildcardQuery(name, "*$query*").caseInsensitive(true)
    }

    fun sortBuilders(pageable: Pageable): List<SortBuilder<*>> {
        val sortBuilders = ArrayList<SortBuilder<*>>()

        pageable.sort.forEach {
            val field = it.property
            val direction = it.direction

            if (field.startsWith("translations.")) {
                val nestedSort = nestedTranslations(field, direction)
                sortBuilders.add(nestedSort)
            } else {
                val fieldSort = field(field, direction)
                sortBuilders.add(fieldSort)
            }
        }

        return sortBuilders
    }

    fun nestedTranslations(field: String, direction: Sort.Direction): FieldSortBuilder {
        return nested("translations", field, direction)
    }

    fun nested(path: String, field: String, direction: Sort.Direction): FieldSortBuilder {
        val nestedSort = NestedSortBuilder(path).setFilter(languageQuery())

        return field(field, direction).setNestedSort(nestedSort)
    }

    fun field(field: String, direction: Sort.Direction): FieldSortBuilder {
        val sortOrder = fromString(direction.toString())

        return fieldSort(field).order(sortOrder)
    }

    fun languageQuery(): TermQueryBuilder {
        val language = RequestContext.language

        return termQuery("translations.language", language)
    }
}