package com.leijendary.domain.sample

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository

interface SampleDocumentRepository : ElasticsearchRepository<SampleDocument, Long>
