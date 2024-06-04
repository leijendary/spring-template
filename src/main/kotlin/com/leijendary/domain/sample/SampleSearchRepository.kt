package com.leijendary.domain.sample

import co.elastic.clients.elasticsearch._types.query_dsl.Query
import co.elastic.clients.util.ObjectBuilder
import com.leijendary.extension.shouldMatch
import com.leijendary.extension.shouldTerm
import com.leijendary.model.Page
import com.leijendary.model.PageRequest
import com.leijendary.model.QueryRequest
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository

interface SampleSearchRepository {
    fun findByTranslations(queryRequest: QueryRequest, pageRequest: PageRequest): Page<SampleSearch>
    fun save(sampleSearch: SampleSearch)
    fun exists(id: Long): Boolean
    fun delete(id: Long)
    fun saveAll(sampleSearches: List<SampleSearch>)
}

private const val FIELD_TRANSLATIONS_NAME = "translations.name"
private const val FIELD_TRANSLATIONS_NAME_KEYWORD = "translations.name.keyword"
private const val FIELD_TRANSLATIONS_DESCRIPTION = "translations.description"

/**
 * Why did I go through all these trouble instead of just using [ElasticsearchRepository]? Simple. Flexibility.
 * Fuzziness is very common, and it is not supported by the simple elasticsearch repository. Utility functions
 * are your friends.
 */
@Repository
class SampleSearchRepositoryImpl(private val elasticsearchTemplate: ElasticsearchTemplate) : SampleSearchRepository {
    init {
        val indexOps = elasticsearchTemplate.indexOps(SampleSearch::class.java)

        if (!indexOps.exists()) {
            indexOps.createWithMapping()
        }
    }

    override fun findByTranslations(queryRequest: QueryRequest, pageRequest: PageRequest): Page<SampleSearch> {
        val query = NativeQueryBuilder()
            .withQuery { buildQuery(it, queryRequest) }
            .withPageable(pageRequest.pageable())
            .build()
        val searchHits = elasticsearchTemplate.search(query, SampleSearch::class.java)
        val list = searchHits.map { it.content }.toList()
        val total = searchHits.totalHits

        return Page(pageRequest, list, total)
    }

    override fun save(sampleSearch: SampleSearch) {
        elasticsearchTemplate.save(sampleSearch)
    }

    override fun exists(id: Long): Boolean {
        return elasticsearchTemplate.exists(id.toString(), SampleSearch::class.java)
    }

    override fun delete(id: Long) {
        elasticsearchTemplate.delete(id.toString(), SampleSearch::class.java)
    }

    override fun saveAll(sampleSearches: List<SampleSearch>) {
        elasticsearchTemplate.save(sampleSearches)
    }

    private fun buildQuery(query: Query.Builder, queryRequest: QueryRequest): ObjectBuilder<Query> {
        if (queryRequest.isEmpty()) {
            return query.matchAll { it }
        }

        return query.bool {
            queryRequest.query?.run {
                it.shouldMatch(this, FIELD_TRANSLATIONS_NAME, FIELD_TRANSLATIONS_DESCRIPTION)
                // Additional boost for the term
                it.shouldTerm(this, FIELD_TRANSLATIONS_NAME_KEYWORD)
            }
        }
    }
}
