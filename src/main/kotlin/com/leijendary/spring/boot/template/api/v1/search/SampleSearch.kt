package com.leijendary.spring.boot.template.api.v1.search

import com.leijendary.spring.boot.template.api.v1.data.SampleSearchResponse
import com.leijendary.spring.boot.template.api.v1.mapper.SampleMapper
import com.leijendary.spring.boot.template.core.data.QueryRequest
import com.leijendary.spring.boot.template.core.exception.ResourceNotFoundException
import com.leijendary.spring.boot.template.core.util.SearchUtil.match
import com.leijendary.spring.boot.template.core.util.SearchUtil.sortBuilders
import com.leijendary.spring.boot.template.document.SampleDocument
import com.leijendary.spring.boot.template.model.SampleTable
import com.leijendary.spring.boot.template.repository.SampleSearchRepository
import org.elasticsearch.index.query.BoolQueryBuilder
import org.elasticsearch.search.sort.SortBuilder
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate
import org.springframework.data.elasticsearch.core.SearchHits
import org.springframework.data.elasticsearch.core.query.NativeSearchQuery
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder
import org.springframework.stereotype.Service
import java.util.*

@Service
class SampleSearch(
    private val elasticsearchRestTemplate: ElasticsearchRestTemplate,
    private val serviceSearchRepository: SampleSearchRepository
) {
    companion object {
        private val MAPPER: SampleMapper = SampleMapper.INSTANCE
        private val SOURCE = listOf("search", "SampleSearch")
    }

    fun page(queryRequest: QueryRequest, pageable: Pageable): Page<SampleSearchResponse> {
        val query: String? = queryRequest.query
        val searchBuilder = NativeSearchQueryBuilder()
        // Query for translations.name and translations.description
        val boolQuery: BoolQueryBuilder = match(query, "translations.name", "translations.description")

        // Add the query for the actual search
        searchBuilder.withQuery(boolQuery)
        // Add the pagination to the search builder
        searchBuilder.withPageable(pageable)

        // Each sort builder should be added into the search builder's sort
        val sortBuilders: List<SortBuilder<*>> = sortBuilders(pageable)
        searchBuilder.withSorts(sortBuilders)

        val searchQuery: NativeSearchQuery = searchBuilder.build()
        val searchHits: SearchHits<SampleDocument?> =
            elasticsearchRestTemplate.search(searchQuery, SampleDocument::class.java)

        val list: List<SampleDocument> = searchHits.map { it.content }.toList()
        val total = searchHits.totalHits

        return PageImpl(list, pageable, total).map { MAPPER.toSearchResponse(it) }
    }

    fun save(sampleTable: SampleTable) {
        val document: SampleDocument = MAPPER.toDocument(sampleTable)

        serviceSearchRepository.save(document)
    }

    fun get(id: UUID): SampleSearchResponse {
        return serviceSearchRepository.findById(id)
            .map { MAPPER.toSearchResponse(it) }
            .orElseThrow { ResourceNotFoundException(SOURCE, id) }
    }

    fun update(sampleTable: SampleTable) {
        val id: UUID = sampleTable.id!!
        val document: SampleDocument = serviceSearchRepository.findById(id)
            .orElseThrow { ResourceNotFoundException(SOURCE, id) }

        MAPPER.update(sampleTable, document)

        serviceSearchRepository.save(document)
    }

    fun delete(id: UUID) {
        val document: SampleDocument = serviceSearchRepository.findById(id)
            .orElseThrow { ResourceNotFoundException(SOURCE, id) }

        serviceSearchRepository.delete(document)
    }
}