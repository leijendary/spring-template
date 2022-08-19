package com.leijendary.spring.template.repository

import com.leijendary.spring.template.document.SampleDocument
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository
import java.util.*

interface SampleSearchRepository : ElasticsearchRepository<SampleDocument, UUID>