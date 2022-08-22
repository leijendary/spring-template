package com.leijendary.spring.template.api.v1.search

import com.leijendary.spring.template.api.v1.data.SampleSearchResponse
import com.leijendary.spring.template.api.v1.mapper.SampleMapper
import com.leijendary.spring.template.core.data.QueryRequest
import com.leijendary.spring.template.core.exception.ResourceNotFoundException
import com.leijendary.spring.template.core.util.SearchQuery.match
import com.leijendary.spring.template.core.util.SearchQuery.sortBuilders
import com.leijendary.spring.template.document.SampleDocument
import com.leijendary.spring.template.model.SampleTable
import com.leijendary.spring.template.repository.SampleSearchRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder
import org.springframework.stereotype.Service
import java.util.*

@Service
class SampleSearch(
    private val elasticsearchRestTemplate: ElasticsearchRestTemplate,
    private val serviceSearchRepository: SampleSearchRepository
) {
    companion object {
        private val MAPPER = SampleMapper.INSTANCE
        private val SOURCE = listOf("search", "SampleSearch", "id")
    }

    fun page(queryRequest: QueryRequest, pageable: Pageable): Page<SampleSearchResponse> {
        val query = queryRequest.query
        val searchBuilder = NativeSearchQueryBuilder()
        // Add the pagination to the search builder
        searchBuilder.withPageable(pageable)

        if (query != null && query.isNotEmpty()) {
            // Query for translations.name and translations.description
            val boolQuery = match(query, "translations.name", "translations.description")
            // Add the query for the actual search
            searchBuilder.withQuery(boolQuery)
        }

        // Each sort builder should be added into the search builder's sort
        val sortBuilders = sortBuilders(pageable.sort)
        searchBuilder.withSorts(sortBuilders)

        val searchQuery = searchBuilder.build()
        val searchHits = elasticsearchRestTemplate.search(searchQuery, SampleDocument::class.java)
        val list = searchHits.map { it.content }.toList()
        val total = searchHits.totalHits

        return PageImpl(list, pageable, total)
            .map { MAPPER.toSearchResponse(it) }
    }

    fun save(sampleTable: SampleTable) {
        val document = MAPPER.toDocument(sampleTable)

        serviceSearchRepository.save(document)
    }

    fun get(id: UUID): SampleSearchResponse {
        return serviceSearchRepository.findById(id)
            .map { MAPPER.toSearchResponse(it) }
            .orElseThrow { ResourceNotFoundException(SOURCE, id) }
    }

    fun update(sampleTable: SampleTable) {
        val id = sampleTable.id
        val document = serviceSearchRepository.findById(id)
            .orElseThrow { ResourceNotFoundException(SOURCE, id) }

        MAPPER.update(sampleTable, document)

        serviceSearchRepository.save(document)
    }

    fun delete(id: UUID) {
        val document = serviceSearchRepository.findById(id)
            .orElseThrow { ResourceNotFoundException(SOURCE, id) }

        serviceSearchRepository.delete(document)
    }
}