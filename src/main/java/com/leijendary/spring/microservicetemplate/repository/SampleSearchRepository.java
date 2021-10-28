package com.leijendary.spring.microservicetemplate.repository;

import com.leijendary.spring.microservicetemplate.document.SampleDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.UUID;

public interface SampleSearchRepository extends ElasticsearchRepository<SampleDocument, UUID> {
}
