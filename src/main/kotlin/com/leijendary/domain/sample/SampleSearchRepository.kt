package com.leijendary.domain.sample

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

interface SampleSearchRepository : ElasticsearchRepository<SampleSearch, Long> {
    fun findByTranslationsNameOrTranslationsDescription(
        name: String,
        description: String,
        pageable: Pageable
    ): Page<SampleSearch>
}
