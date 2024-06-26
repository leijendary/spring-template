package com.leijendary.domain.sample

import co.elastic.clients.elasticsearch._types.ScriptLanguage
import co.elastic.clients.elasticsearch._types.query_dsl.Query
import co.elastic.clients.util.ObjectBuilder
import com.leijendary.extension.content
import com.leijendary.extension.shouldMatch
import com.leijendary.extension.shouldTerm
import com.leijendary.model.ErrorSource
import com.leijendary.model.Page
import com.leijendary.model.PageRequest
import com.leijendary.model.QueryRequest
import com.leijendary.projection.ImageProjection
import jakarta.annotation.PostConstruct
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates
import org.springframework.data.elasticsearch.core.query.ScriptType.STORED
import org.springframework.data.elasticsearch.core.query.UpdateQuery
import org.springframework.data.elasticsearch.core.script.Script
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository

/**
 * [ElasticsearchRepository] for automatic index creation, and GraalVM reflection registration.
 * Without this, you have to manually indicate the data classes for reflection.
 */
interface SampleSearchRepository : SampleSearchCustomRepository, ElasticsearchRepository<SampleSearch, Long>

interface SampleSearchCustomRepository {
    fun findByTranslations(queryRequest: QueryRequest, pageRequest: PageRequest): Page<SampleSearch>
    fun setImage(id: Long, image: ImageProjection)
    fun deleteImage(id: Long)
}

const val ENTITY_SEARCH = SampleSearch.INDEX_NAME
val SOURCE_SEARCH = ErrorSource(pointer = "/search/$ENTITY_SEARCH/id")

private val INDEX = IndexCoordinates.of(ENTITY_SEARCH)
private val SCRIPT_IMAGE_SET = Script.builder()
    .withId("sample-image-set")
    .withSource("elasticsearch/sample/image.set.groovy".content())
    .withLanguage(ScriptLanguage.Painless.jsonValue())
    .build()
private val SCRIPT_IMAGE_DELETE = Script.builder()
    .withId("sample-image-delete")
    .withSource("elasticsearch/sample/image.delete.groovy".content())
    .withLanguage(ScriptLanguage.Painless.jsonValue())
    .build()
private val SCRIPTS = arrayOf(SCRIPT_IMAGE_SET, SCRIPT_IMAGE_DELETE)
private const val FIELD_TRANSLATIONS_NAME = "translations.name"
private const val FIELD_TRANSLATIONS_NAME_KEYWORD = "translations.name.keyword"
private const val FIELD_TRANSLATIONS_DESCRIPTION = "translations.description"
private const val PARAM_IMAGE = "image"

@Repository
class SampleSearchCustomRepositoryImpl(private val elasticsearchTemplate: ElasticsearchTemplate) :
    SampleSearchCustomRepository {
    @PostConstruct
    fun init() {
        SCRIPTS.forEach(elasticsearchTemplate::putScript)
    }

    override fun findByTranslations(queryRequest: QueryRequest, pageRequest: PageRequest): Page<SampleSearch> {
        val query = NativeQueryBuilder()
            .withQuery { buildQuery(it, queryRequest) }
            .withPageable(pageRequest.pageable)
            .build()
        val searchHits = elasticsearchTemplate.search(query, SampleSearch::class.java)
        val list = searchHits.map { it.content }.toList()

        return Page(pageRequest, list, searchHits.totalHits)
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
        if (queryRequest.isEmpty) {
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
