package com.leijendary.spring.template.repository

import com.leijendary.spring.template.core.exception.ResourceNotFoundException
import com.leijendary.spring.template.document.SampleDocument
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import org.springframework.data.repository.findByIdOrNull
import java.util.*

private val source = listOf("search", "SampleSearch", "id")

interface SampleSearchRepository : ElasticsearchRepository<SampleDocument, UUID> {
    fun findByIdOrThrow(id: UUID) = findByIdOrNull(id) ?: throw ResourceNotFoundException(source, id)
}
