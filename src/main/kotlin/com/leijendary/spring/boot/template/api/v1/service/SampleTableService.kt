package com.leijendary.spring.boot.template.api.v1.service

import com.leijendary.spring.boot.template.api.v1.data.SampleRequest
import com.leijendary.spring.boot.template.api.v1.data.SampleResponse
import com.leijendary.spring.boot.template.api.v1.mapper.SampleMapper
import com.leijendary.spring.boot.template.api.v1.search.SampleSearch
import com.leijendary.spring.boot.template.core.data.QueryRequest
import com.leijendary.spring.boot.template.core.data.Seek
import com.leijendary.spring.boot.template.core.data.Seekable
import com.leijendary.spring.boot.template.core.exception.ResourceNotFoundException
import com.leijendary.spring.boot.template.data.SampleCreateEvent
import com.leijendary.spring.boot.template.data.SampleDeleteEvent
import com.leijendary.spring.boot.template.data.SampleUpdateEvent
import com.leijendary.spring.boot.template.model.SampleTable
import com.leijendary.spring.boot.template.repository.SampleTableRepository
import com.leijendary.spring.boot.template.specification.SampleListSpecification
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class SampleTableService(
    private val applicationEventPublisher: ApplicationEventPublisher,
    private val sampleSearch: SampleSearch,
    private val sampleTableRepository: SampleTableRepository,
) {
    companion object {
        const val CACHE_NAME = "sample:v1"
        private val MAPPER: SampleMapper = SampleMapper.INSTANCE
        private val SOURCE = listOf("data", "SampleTable", "id")
    }

    @Transactional(readOnly = true)
    fun seek(queryRequest: QueryRequest, seekable: Seekable): Seek<SampleResponse> {
        val specification: Specification<SampleTable> = SampleListSpecification(queryRequest.query)

        return sampleTableRepository
            .findAll(SampleTable::class, specification, seekable)
            .map { MAPPER.toResponse(it) }
    }

    @Transactional
    fun create(sampleRequest: SampleRequest): SampleResponse {
        val sampleTable: SampleTable = MAPPER.toEntity(sampleRequest)

        sampleTableRepository.save(sampleTable)

        applicationEventPublisher.publishEvent(SampleCreateEvent(sampleTable))

        return MAPPER.toResponse(sampleTable)
    }

    @Transactional(readOnly = true)
    fun get(id: UUID): SampleResponse {
        return sampleTableRepository.findById(id)
            .map { MAPPER.toResponse(it) }
            .orElseThrow { ResourceNotFoundException(SOURCE, id) }
    }

    @Transactional
    fun update(id: UUID, sampleRequest: SampleRequest): SampleResponse {
        val sampleTable: SampleTable = sampleTableRepository.findLockedById(id)
            ?: throw ResourceNotFoundException(SOURCE, id)

        MAPPER.update(sampleRequest, sampleTable)

        sampleTableRepository.save(sampleTable)

        applicationEventPublisher.publishEvent(SampleUpdateEvent(sampleTable))

        return MAPPER.toResponse(sampleTable)
    }

    @Transactional
    fun delete(id: UUID) {
        val sampleTable: SampleTable = sampleTableRepository.findLockedById(id)
            ?: throw ResourceNotFoundException(SOURCE, id)

        sampleTableRepository.softDelete(sampleTable)

        applicationEventPublisher.publishEvent(SampleDeleteEvent(sampleTable))
    }

    @Transactional(readOnly = true)
    fun reindex() {
        sampleTableRepository.streamAll()
            .parallel()
            .forEach { sampleSearch.save(it) }
    }
}