package com.leijendary.domain.sample

import co.elastic.clients.elasticsearch._types.ScriptLanguage
import com.leijendary.model.ImageProjection
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.annotations.Query
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates
import org.springframework.data.elasticsearch.core.query.ScriptType.INLINE
import org.springframework.data.elasticsearch.core.query.UpdateQuery
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.stereotype.Repository

private val INDEX = IndexCoordinates.of(SampleSearch.INDEX_NAME)
private const val QUERY_FIND_BY_TRANSLATIONS = """
{
  "bool": {
    "should": [
      { "match": { "translations.name": { "query": "#{#query}", "fuzziness": "auto" } } },
      { "match": { "translations.description": { "query": "#{#query}", "fuzziness": "auto" } } },
      { "term": { "translations.name.keyword": { "value": "#{#query}", "boost": 2.0, "case_insensitive": true } } }
    ],
    "minimum_should_match": "1"
  }
}
"""
private const val SCRIPT_IMAGE_SET = "ctx._source.image = params.image"
private const val SCRIPT_IMAGE_DELETE = "ctx._source.remove('image')"
private const val PARAM_IMAGE = "image"

interface SampleSearchRepository : SampleSearchCustomRepository, ElasticsearchRepository<SampleSearch, String> {
    @Query(QUERY_FIND_BY_TRANSLATIONS)
    fun findByTranslations(query: String, pageable: Pageable): Page<SampleSearch>
}

interface SampleSearchCustomRepository {
    fun setImage(id: String, image: ImageProjection)

    fun deleteImage(id: String)
}

@Repository
class SampleSearchCustomRepositoryImpl(private val elasticsearchTemplate: ElasticsearchTemplate) :
    SampleSearchCustomRepository {
    override fun setImage(id: String, image: ImageProjection) {
        val params = mapOf(PARAM_IMAGE to image)
        val update = UpdateQuery.builder(id)
            .withParams(params)
            .withScript(SCRIPT_IMAGE_SET)
            .withScriptType(INLINE)
            .withLang(ScriptLanguage.Painless.jsonValue())
            .withAbortOnVersionConflict(false)
            .build()

        elasticsearchTemplate.update(update, INDEX)
    }

    override fun deleteImage(id: String) {
        val update = UpdateQuery.builder(id)
            .withScript(SCRIPT_IMAGE_DELETE)
            .withScriptType(INLINE)
            .withLang(ScriptLanguage.Painless.jsonValue())
            .withAbortOnVersionConflict(false)
            .build()

        elasticsearchTemplate.update(update, INDEX)
    }
}
