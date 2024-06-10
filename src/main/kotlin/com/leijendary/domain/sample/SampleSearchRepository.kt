package com.leijendary.domain.sample

import co.elastic.clients.elasticsearch._types.ScriptLanguage
import co.elastic.clients.elasticsearch._types.query_dsl.Query
import co.elastic.clients.util.ObjectBuilder
import com.leijendary.extension.shouldMatch
import com.leijendary.extension.shouldTerm
import com.leijendary.model.ErrorSource
import com.leijendary.model.Page
import com.leijendary.model.PageRequest
import com.leijendary.model.QueryRequest
import com.leijendary.projection.ImageProjection
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates
import org.springframework.data.elasticsearch.core.query.ScriptType.STORED
import org.springframework.data.elasticsearch.core.query.UpdateQuery
import org.springframework.data.elasticsearch.core.script.Script
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository

interface SampleSearchRepository {
    fun findByTranslations(queryRequest: QueryRequest, pageRequest: PageRequest): Page<SampleSearch>
    fun save(sampleSearch: SampleSearch)
    fun exists(id: Long): Boolean
    fun delete(id: Long)
    fun saveAll(sampleSearches: List<SampleSearch>)
    fun setImage(id: Long, image: ImageProjection)
    fun deleteImage(id: Long)
}

const val SEARCH_ENTITY = "sampleSearch"
val SEARCH_SOURCE = ErrorSource(pointer = "/data/$SEARCH_ENTITY/id")

private val INDEX = IndexCoordinates.of(SampleSearch.INDEX_NAME)
private val SCRIPT_IMAGE_SET = Script.builder()
    .withId("sample-image-set")
    .withSource("ctx._source.image = params.image")
    .withLanguage(ScriptLanguage.Painless.jsonValue())
    .build()
private val SCRIPT_IMAGE_DELETE = Script.builder()
    .withId("sample-image-delete")
    .withSource("ctx._source.remove('image')")
    .withLanguage(ScriptLanguage.Painless.jsonValue())
    .build()
private const val FIELD_TRANSLATIONS_NAME = "translations.name"
private const val FIELD_TRANSLATIONS_NAME_KEYWORD = "translations.name.keyword"
private const val FIELD_TRANSLATIONS_DESCRIPTION = "translations.description"
private const val PARAM_IMAGE = "image"

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

        arrayOf(SCRIPT_IMAGE_SET, SCRIPT_IMAGE_DELETE).forEach(elasticsearchTemplate::putScript)
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

    override fun setImage(id: Long, image: ImageProjection) {
        val params = mapOf(PARAM_IMAGE to image)
        val update = UpdateQuery
            .builder(id.toString())
            .withScript(SCRIPT_IMAGE_SET.id)
            .withScriptType(STORED)
            .withParams(params)
            .withAbortOnVersionConflict(false)
            .build()

        elasticsearchTemplate.update(update, INDEX)
    }

    override fun deleteImage(id: Long) {
        val update = UpdateQuery
            .builder(id.toString())
            .withScript(SCRIPT_IMAGE_DELETE.id)
            .withScriptType(STORED)
            .withAbortOnVersionConflict(false)
            .build()

        elasticsearchTemplate.update(update, INDEX)
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
