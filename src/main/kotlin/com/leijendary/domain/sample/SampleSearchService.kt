package com.leijendary.domain.sample

import com.leijendary.context.RequestContext.language
import com.leijendary.domain.image.ImageResponse
import com.leijendary.domain.image.ImageService
import com.leijendary.domain.sample.SampleSearch.Companion.ERROR_SOURCE_SEARCH
import com.leijendary.domain.sample.SampleSearch.Companion.INDEX_NAME
import com.leijendary.error.exception.ResourceNotFoundException
import com.leijendary.extension.logger
import com.leijendary.model.QueryRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.elasticsearch.core.suggest.Completion
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.atomic.AtomicInteger
import kotlin.streams.asSequence

interface SampleSearchService {
    fun page(queryRequest: QueryRequest, pageable: Pageable): Page<SampleResponse>
    fun save(sample: SampleDetailResponse)
    fun update(sample: SampleDetailResponse)
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

    override fun save(sample: SampleDetailResponse) {
        val search = map(sample)

        sampleSearchRepository.save(search)
    }

    override fun update(sample: SampleDetailResponse) {
        val exists = sampleSearchRepository.existsById(sample.id)

        if (!exists) {
            throw ResourceNotFoundException(sample.id, INDEX_NAME, ERROR_SOURCE_SEARCH)
        }

        save(sample)
    }

    override fun delete(id: String) {
        val exists = sampleSearchRepository.existsById(id)

        if (!exists) {
            throw ResourceNotFoundException(id, INDEX_NAME, ERROR_SOURCE_SEARCH)
        }

        sampleSearchRepository.deleteById(id)
    }

    @Transactional(readOnly = true)
    override fun reindex(): Int {
        val count = AtomicInteger()

        sampleRepository.streamBy(SampleDetailResponse::class.java)
            .parallel()
            .map(::mapStream)
            .use {
                it.asSequence()
                    .chunked(STREAM_CHUNK)
                    .forEach { list ->
                        sampleSearchRepository.saveAll(list)

                        val current = count.addAndGet(list.size)

                        log.info("Synced $current samples.")
                    }
            }

        return count.get()
    }

    private fun map(sample: SampleDetailResponse) = SampleSearch(
        id = sample.id,
        name = sample.name,
        description = sample.description,
        amount = sample.amount,
        translations = sample.translations.map {
            SampleTranslationSearch(
                name = it.name,
                description = it.description,
                language = it.language,
                ordinal = it.ordinal,
            )
        },
        image = sample.image,
        createdAt = sample.createdAt,
        completion = sample.translations
            .map { it.name }
            .let(::Completion),
    )

    private fun mapToList(search: SampleSearch): SampleResponse {
        val translation = search.getTranslation(language)
        val result = SampleResponse(
            id = search.id,
            name = translation.name,
            description = translation.description,
            amount = search.amount,
            createdAt = search.createdAt,
        )
        result.image = search.image?.let(imageService::getPublicUrl)

        return result
    }

    private fun mapStream(sample: SampleDetailResponse): SampleSearch {
        sample.image = sampleImageRepository.findByIdOrNull(sample.id, ImageResponse::class.java)

        // Add the translations of the entity
        val translations = sampleTranslationRepository.findById(sample.id, SampleTranslationResponse::class.java)
        sample.translations.addAll(translations)

        return map(sample)
    }
}
