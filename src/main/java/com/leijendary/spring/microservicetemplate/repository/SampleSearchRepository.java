package com.leijendary.spring.microservicetemplate.repository;

import com.leijendary.spring.microservicetemplate.document.SampleDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface SampleSearchRepository extends ElasticsearchRepository<SampleDocument, Long> {
}
