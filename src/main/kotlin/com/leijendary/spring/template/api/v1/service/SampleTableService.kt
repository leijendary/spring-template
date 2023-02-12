package com.leijendary.spring.template.api.v1.service

import com.leijendary.spring.template.api.v1.mapper.SampleMapper
import com.leijendary.spring.template.api.v1.model.SampleRequest
import com.leijendary.spring.template.api.v1.model.SampleResponse
import com.leijendary.spring.template.api.v1.search.SampleSearch
import com.leijendary.spring.template.core.exception.ResourceNotFoundException
import com.leijendary.spring.template.core.extension.afterCommit
import com.leijendary.spring.template.core.model.QueryRequest
import com.leijendary.spring.template.core.model.Seek
import com.leijendary.spring.template.core.model.Seekable
import com.leijendary.spring.template.entity.SampleTable
import com.leijendary.spring.template.event.SampleTableEvent
import com.leijendary.spring.template.repository.SampleTableRepository
import com.leijendary.spring.template.specification.SampleListSpecification
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.CachePut
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.concurrent.atomic.AtomicInteger

@Service
class SampleTableService(
    private val sampleTableEvent: SampleTableEvent,
    private val sampleSearch: SampleSearch,
    private val sampleTableRepository: SampleTableRepository,
) {
    companion object {
        private const val CACHE_NAME = "sample:v1"
        private val MAPPER = SampleMapper.INSTANCE
        private val SOURCE = listOf("data", "SampleTable", "id")
    }

    @Transactional(readOnly = true)
    fun seek(queryRequest: QueryRequest, seekable: Seekable): Seek<SampleResponse> {
        val specification = SampleListSpecification(queryRequest.query)

        return sampleTableRepository
            .findAll(SampleTable::class, specification, seekable)
            .map { MAPPER.toResponse(it) }
    }

    @CachePut(value = [CACHE_NAME], key = "#result.id")
    @Transactional
    fun create(sampleRequest: SampleRequest): SampleResponse {
        val sampleTable = MAPPER.toEntity(sampleRequest).let { sampleTableRepository.save(it) }

        afterCommit {
            sampleTableEvent.create(sampleTable)
        }

        return MAPPER.toResponse(sampleTable)
    }

    @Cacheable(value = [CACHE_NAME], key = "#id")
    @Transactional(readOnly = true)
    fun get(id: UUID): SampleResponse {
        return sampleTableRepository
            .findById(id)
            .map { MAPPER.toResponse(it) }
            .orElseThrow { ResourceNotFoundException(SOURCE, id) }
    }

    @CachePut(value = [CACHE_NAME], key = "#result.id")
    @Transactional
    fun update(id: UUID, sampleRequest: SampleRequest): SampleResponse {
        val sampleTable = sampleTableRepository
            .findLockedById(id)
            ?.let {
                MAPPER.update(sampleRequest, it)

                sampleTableRepository.save(it)
            }
            ?: throw ResourceNotFoundException(SOURCE, id)

        afterCommit {
            sampleTableEvent.update(sampleTable)
        }

        return MAPPER.toResponse(sampleTable)
    }

    @CacheEvict(value = [CACHE_NAME], key = "#id")
    @Transactional
    fun delete(id: UUID) {
        val sampleTable = sampleTableRepository
            .findLockedById(id)
            ?: throw ResourceNotFoundException(SOURCE, id)

        sampleTableRepository.softDelete(sampleTable)

        afterCommit {
            sampleTableEvent.delete(sampleTable)
        }
    }

    @Transactional(readOnly = true)
    fun reindex(): Int {
        val count = AtomicInteger()

        sampleTableRepository
            .streamAllByDeletedAtIsNull()
            .parallel()
            .forEach {
                sampleSearch.save(it)

                count.incrementAndGet()
            }

        return count.get()
    }
}
