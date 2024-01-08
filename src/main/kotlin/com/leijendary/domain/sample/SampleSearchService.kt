package com.leijendary.domain.sample

import com.leijendary.extension.logger
import com.leijendary.model.Page
import com.leijendary.model.Page.Companion.empty
import com.leijendary.model.PageRequest
import com.leijendary.model.QueryRequest
import org.springframework.data.elasticsearch.core.suggest.Completion
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicInteger
import kotlin.streams.asSequence

private const val STREAM_CHUNK = 1000

@Service
class SampleSearchService(
    private val sampleRepository: SampleRepository,
    private val sampleSearchRepository: SampleSearchRepository,
) {
    private val log = logger()

    fun page(queryRequest: QueryRequest, pageRequest: PageRequest): Page<SampleList> {
        if (queryRequest.query.isNullOrBlank()) {
            return empty(pageRequest)
        }

        val query = queryRequest.query
        val samples = sampleSearchRepository
            .findByTranslationsNameOrTranslationsDescription(query, query, pageRequest.pageable())
            .map(::mapToList)

        return Page(pageRequest, samples.content, samples.totalElements)
    }

    fun save(sample: SampleDetail) {
        val search = map(sample)

        sampleSearchRepository.save(search)
    }

    fun delete(id: Long) {
        sampleSearchRepository.deleteById(id)
    }

    fun reindex(): Int {
        val count = AtomicInteger()

        sampleRepository.streamAll().use { stream ->
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
            .toTypedArray()
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

    private fun map(search: SampleSearch): SampleDetail {
        val translation = search.translation

        return SampleDetail(
            id = search.id,
            name = translation.name,
            description = translation.description,
            amount = search.amount,
            version = 0,
            createdAt = search.createdAt,
        )
    }

    private fun mapStream(sample: SampleDetail): SampleSearch {
        val translations = sampleRepository.listTranslations(sample.id)
        sample.translations.addAll(translations)

        return map(sample)
    }
}
