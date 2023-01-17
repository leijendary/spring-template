package com.leijendary.spring.template.api.v1.search

import com.leijendary.spring.template.api.v1.data.SampleSearchResponse
import com.leijendary.spring.template.api.v1.mapper.SampleMapper
import com.leijendary.spring.template.core.data.QueryRequest
import com.leijendary.spring.template.core.exception.ResourceNotFoundException
import com.leijendary.spring.template.core.extension.shouldMatch
import com.leijendary.spring.template.core.extension.sortBuilder
import com.leijendary.spring.template.document.SampleDocument
import com.leijendary.spring.template.model.SampleTable
import com.leijendary.spring.template.repository.SampleSearchRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder
import org.springframework.stereotype.Service
import java.util.*

@Service
class SampleSearch(
    private val elasticsearchTemplate: ElasticsearchTemplate,
    private val serviceSearchRepository: SampleSearchRepository
) {
    companion object {
        private val MAPPER = SampleMapper.INSTANCE
        private val SOURCE = listOf("search", "SampleSearch", "id")
    }

    fun page(queryRequest: QueryRequest, pageable: Pageable): Page<SampleSearchResponse> {
        val query = queryRequest.query
        val searchBuilder = NativeQueryBuilder()
        // Add the pagination to the search builder
        searchBuilder.withPageable(pageable)

        if (!query.isNullOrEmpty()) {
            // Add the query for the actual search
            searchBuilder.withQuery { builder ->
                builder.bool { bool ->
                    bool.shouldMatch(query, "translations.name", "translations.description")
                }
            }
        }

        // Each sort builder should be added into the search builder's sort
        searchBuilder.withSort { sort ->
            sort.sortBuilder(pageable.sort)
        }

        val searchQuery = searchBuilder.build()
        val searchHits = elasticsearchTemplate.search(searchQuery, SampleDocument::class.java)
        val list = searchHits
            .map { MAPPER.toSearchResponse(it.content) }
            .toList()
        val total = searchHits.totalHits

        return PageImpl(list, pageable, total)
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
