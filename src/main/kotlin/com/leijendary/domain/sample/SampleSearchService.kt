package com.leijendary.domain.sample

import com.leijendary.context.RequestContext.language
import com.leijendary.domain.image.ImageService
import com.leijendary.domain.sample.SampleSearch.Companion.INDEX_NAME
import com.leijendary.domain.sample.SampleSearch.Companion.POINTER_ID
import com.leijendary.error.exception.ResourceNotFoundException
import com.leijendary.extension.logger
import com.leijendary.extension.supplyAsyncSpan
import com.leijendary.model.QueryRequest
import io.micrometer.tracing.Tracer
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.atomic.AtomicInteger
import kotlin.streams.asSequence

interface SampleSearchService {
    fun page(queryRequest: QueryRequest, pageable: Pageable): Page<SampleResponse>
    fun save(message: SampleMessage)
    fun update(message: SampleMessage)
    fun delete(id: String)
    fun reindex(): Int
}

private const val STREAM_CHUNK = 1000

@Service
class SampleSearchServiceImpl(
    private val imageService: ImageService,
    private val sampleImageRepository: SampleImageRepository,
    private val sampleRepository: SampleRepository,
    private val sampleSearchRepository: SampleSearchRepository,
    private val sampleTranslationRepository: SampleTranslationRepository,
    private val tracer: Tracer
) : SampleSearchService {
    private val log = logger()

    override fun page(queryRequest: QueryRequest, pageable: Pageable): Page<SampleResponse> {
        val page = if (!queryRequest.query.isNullOrBlank()) {
            sampleSearchRepository.findByTranslations(queryRequest.query, pageable)
        } else {
            sampleSearchRepository.findAll(pageable)
        }

        return page.map(::mapToList)
    }

    override fun save(message: SampleMessage) {
        val search = SampleMapperImpl.toSearch(message)

        sampleSearchRepository.save(search)

        log.info("Saved $INDEX_NAME {} with payload {}", search.id, search)
    }

    override fun update(message: SampleMessage) {
        val exists = sampleSearchRepository.existsById(message.id)

        if (!exists) {
            throw ResourceNotFoundException(message.id, INDEX_NAME, POINTER_ID)
        }

        val search = SampleMapperImpl.toSearch(message)

        sampleSearchRepository.save(search)

        log.info("Updated $INDEX_NAME {} with payload {}", message.id, message)
    }

    override fun delete(id: String) {
        val exists = sampleSearchRepository.existsById(id)

        if (!exists) {
            throw ResourceNotFoundException(id, INDEX_NAME, POINTER_ID)
        }

        sampleSearchRepository.deleteById(id)

        log.info("Deleted $INDEX_NAME {}", id)
    }

    @Transactional(readOnly = true)
    override fun reindex(): Int {
        val count = AtomicInteger()

        sampleRepository.streamBy(SampleDetailResponse::class.java).parallel().use { stream ->
            stream.asSequence().chunked(STREAM_CHUNK).forEach { list ->
                val mapped = mapList(list)

                sampleSearchRepository.saveAll(mapped)

                val current = count.addAndGet(list.size)

                log.info("Synced $current samples.")
            }
        }

        return count.get()
    }

    private fun mapToList(search: SampleSearch): SampleResponse {
        val image = search.image?.let(imageService::getPublicUrl)

        return SampleMapperImpl.toResponse(search, language, image)
    }

    private fun mapList(samples: List<SampleDetailResponse>): List<SampleSearch> {
        val ids = samples.mapTo(mutableSetOf()) { it.id }
        val imagesFuture = tracer.supplyAsyncSpan {
            sampleImageRepository.findAllById(ids).associateBy { it.id }
        }
        val translationsFuture = tracer.supplyAsyncSpan {
            sampleTranslationRepository.findAllById(ids)
                .groupBy { it.id }
                .mapValues { it.value.map(SampleMapperImpl::toResponse) }
        }
        val images = imagesFuture.get()
        val translations = translationsFuture.get()

        return samples.map {
            it.apply {
                this.image = images[id]
                this.translations.addAll(translations[id] ?: mutableListOf())
            }

            SampleMapperImpl.toSearch(it)
        }
    }
}
