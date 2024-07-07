package com.leijendary.domain.sample

import com.leijendary.domain.image.ImageService
import com.leijendary.error.exception.ResourceNotFoundException
import com.leijendary.extension.logger
import com.leijendary.model.Page
import com.leijendary.model.PageRequest
import com.leijendary.model.QueryRequest
import org.springframework.data.elasticsearch.core.suggest.Completion
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.atomic.AtomicInteger
import kotlin.streams.asSequence

interface SampleSearchService {
    fun page(queryRequest: QueryRequest, pageRequest: PageRequest): Page<SampleList>
    fun save(sample: SampleDetail)
    fun update(sample: SampleDetail)
    fun delete(id: Long)
    fun reindex(): Int
}

private const val STREAM_CHUNK = 1000

@Service
class SampleSearchServiceImpl(
    private val imageService: ImageService,
    private val sampleRepository: SampleRepository,
    private val sampleSearchRepository: SampleSearchRepository,
) : SampleSearchService {
    private val log = logger()

    override fun page(queryRequest: QueryRequest, pageRequest: PageRequest): Page<SampleList> {
        return sampleSearchRepository
            .findByTranslations(queryRequest, pageRequest)
            .map(::mapToList)
    }

    override fun save(sample: SampleDetail) {
        val search = map(sample)

        sampleSearchRepository.save(search)
    }

    override fun update(sample: SampleDetail) {
        val exists = sampleSearchRepository.existsById(sample.id)

        if (!exists) {
            throw ResourceNotFoundException(sample.id, ENTITY_SEARCH, SOURCE_SEARCH)
        }

        save(sample)
    }

    override fun delete(id: Long) {
        val exists = sampleSearchRepository.existsById(id)

        if (!exists) {
            throw ResourceNotFoundException(id, ENTITY_SEARCH, SOURCE_SEARCH)
        }

        sampleSearchRepository.deleteById(id)
    }

    @Transactional(readOnly = true)
    override fun reindex(): Int {
        val count = AtomicInteger()

        sampleRepository.streamAll()
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

    private fun map(sample: SampleDetail) = SampleSearch(
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

    private fun mapToList(search: SampleSearch): SampleList {
        val translation = search.translation
        val result = SampleList(
            id = search.id,
            name = translation.name,
            description = translation.description,
            amount = search.amount,
            createdAt = search.createdAt,
        )
        result.image = search.image?.let(imageService::getPublicUrl)

        return result
    }

    private fun mapStream(sample: SampleDetail): SampleSearch {
        val translations = sampleRepository.listTranslations(sample.id)
        sample.translations.addAll(translations)

        return map(sample)
    }
}
