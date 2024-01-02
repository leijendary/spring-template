package com.leijendary.domain.sample

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

interface SampleDocumentRepository : ElasticsearchRepository<SampleDocument, Long> {
    fun findByTranslationsNameOrTranslationsDescription(
        name: String,
        description: String,
        pageable: Pageable
    ): Page<SampleDocument>
}
