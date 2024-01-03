package com.leijendary.domain.sample

import com.leijendary.error.exception.ResourceNotFoundException
import com.leijendary.model.ErrorSource
import com.leijendary.model.Page
import com.leijendary.model.Page.Companion.empty
import com.leijendary.model.PageRequest
import com.leijendary.model.QueryRequest
import org.springframework.data.elasticsearch.core.suggest.Completion
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicInteger
import kotlin.streams.asSequence

private const val STREAM_CHUNK = 1000
private const val ENTITY = "sampleDocument"
private val SOURCE = ErrorSource(pointer = "/data/$ENTITY/id")

@Service
class SampleSearchService(
    private val sampleDocumentRepository: SampleDocumentRepository,
    private val sampleRepository: SampleRepository,
) {
    fun page(queryRequest: QueryRequest, pageRequest: PageRequest): Page<SampleList> {
        if (queryRequest.query.isNullOrBlank()) {
            return empty(pageRequest)
        }

        val query = queryRequest.query
        val samples = sampleDocumentRepository
            .findByTranslationsNameOrTranslationsDescription(query, query, pageRequest.pageable())
            .map(::mapToList)

        return Page(pageRequest, samples.content, samples.totalElements)
    }

    fun save(sample: SampleDetail) {
        val document = map(sample)

        sampleDocumentRepository.save(document)
    }

    fun get(id: Long): SampleDetail {
        val document = sampleDocumentRepository.findByIdOrNull(id)
            ?: throw ResourceNotFoundException(id, ENTITY, SOURCE)

        return map(document)
    }

    fun update(sample: SampleDetail) {
        val document = sampleDocumentRepository.findByIdOrNull(sample.id) ?: return
        document.update(sample)

        sampleDocumentRepository.save(document)
    }

    fun delete(id: Long) {
        sampleDocumentRepository.deleteById(id)
    }

    fun reindex(): Int {
        val count = AtomicInteger()

        sampleRepository.streamAll().parallel().use { stream ->
            stream.map(::mapStream)
                .asSequence()
                .chunked(STREAM_CHUNK)
                .forEach {
                    sampleDocumentRepository.saveAll(it)
                    count.addAndGet(it.size)
                }
        }

        return count.get()
    }

    private fun map(sample: SampleDetail) = SampleDocument(
        id = sample.id,
        name = sample.name,
        description = sample.description,
        amount = sample.amount,
        translations = sample.translations.map {
            SampleTranslationDocument(
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

    private fun mapToList(document: SampleDocument): SampleList {
        val translation = document.translation

        return SampleList(
            id = document.id,
            name = translation.name,
            description = translation.description,
            amount = document.amount,
            createdAt = document.createdAt,
        )
    }

    private fun map(document: SampleDocument): SampleDetail {
        val translation = document.translation

        return SampleDetail(
            id = document.id,
            name = translation.name,
            description = translation.description,
            amount = document.amount,
            version = 0,
            createdAt = document.createdAt,
        )
    }

    private fun mapStream(sample: SampleDetail): SampleDocument {
        val translations = sampleRepository.listTranslations(sample.id)
        sample.translations.addAll(translations)

        return map(sample)
    }
}
