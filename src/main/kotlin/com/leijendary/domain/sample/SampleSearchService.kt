package com.leijendary.domain.sample

import com.leijendary.error.exception.ResourceNotFoundException
import com.leijendary.extension.logger
import com.leijendary.model.ErrorSource
import com.leijendary.model.Page
import com.leijendary.model.Page.Companion.empty
import com.leijendary.model.PageRequest
import com.leijendary.model.QueryRequest
import org.springframework.data.elasticsearch.core.suggest.Completion
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.atomic.AtomicInteger
import kotlin.streams.asSequence

private const val STREAM_CHUNK = 1000
private const val ENTITY = "sampleSearch"
private val SOURCE = ErrorSource(pointer = "/data/$ENTITY/id")

interface SampleSearchService {
    fun page(queryRequest: QueryRequest, pageRequest: PageRequest): Page<SampleList>
    fun save(sample: SampleDetail)
    fun update(sample: SampleDetail)
    fun delete(id: Long)
    fun reindex(): Int
}

@Service
class SampleSearchServiceImpl(
    private val sampleRepository: SampleRepository,
    private val sampleSearchRepository: SampleSearchRepository,
) : SampleSearchService {
    private val log = logger()

    override fun page(queryRequest: QueryRequest, pageRequest: PageRequest): Page<SampleList> {
        if (queryRequest.query.isNullOrBlank()) {
            return empty(pageRequest)
        }

        val query = queryRequest.query
        val samples = sampleSearchRepository
            .findByTranslationsNameOrTranslationsDescription(query, query, pageRequest.pageable())
            .map(::mapToList)

        return Page(pageRequest, samples.content, samples.totalElements)
    }

    override fun save(sample: SampleDetail) {
        val search = map(sample)

        sampleSearchRepository.save(search)
    }

    override fun update(sample: SampleDetail) {
        val exists = sampleSearchRepository.existsById(sample.id)

        if (!exists) {
            throw ResourceNotFoundException(sample.id, ENTITY, SOURCE)
        }

        save(sample)
    }

    override fun delete(id: Long) {
        val exists = sampleSearchRepository.existsById(id)

        if (!exists) {
            throw ResourceNotFoundException(id, ENTITY, SOURCE)
        }

        sampleSearchRepository.deleteById(id)
    }

    @Transactional(readOnly = true)
    override fun reindex(): Int {
        val count = AtomicInteger()

        sampleRepository.streamAll().parallel().use { stream ->
            stream.map(::mapStream)
                .asSequence()
                .chunked(STREAM_CHUNK)
                .forEach {
                    sampleSearchRepository.saveAll(it)

                    val current = count.addAndGet(it.size)

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
            SampleSearchTranslation(
                name = it.name,
                description = it.description,
                language = it.language,
                ordinal = it.ordinal,
            )
        },
        createdAt = sample.createdAt,
        completion = sample.translations
            .map { it.name }
            .let(::Completion),
    )

    private fun mapToList(search: SampleSearch): SampleList {
        val translation = search.translation

        return SampleList(
            id = search.id,
            name = translation.name,
            description = translation.description,
            amount = search.amount,
            createdAt = search.createdAt,
        )
    }

    private fun mapStream(sample: SampleDetail): SampleSearch {
        val translations = sampleRepository.listTranslations(sample.id)
        sample.translations.addAll(translations)

        return map(sample)
    }
}
