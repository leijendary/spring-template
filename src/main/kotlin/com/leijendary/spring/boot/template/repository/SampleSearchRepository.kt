package com.leijendary.spring.boot.template.repository

import com.leijendary.spring.boot.template.document.SampleDocument
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import java.util.*

interface SampleSearchRepository : ElasticsearchRepository<SampleDocument, UUID>