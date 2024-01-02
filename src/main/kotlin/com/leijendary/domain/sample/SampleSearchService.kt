package com.leijendary.domain.sample

import com.leijendary.error.exception.ResourceNotFoundException
import com.leijendary.extension.shouldMatch
import com.leijendary.model.ErrorSource
import com.leijendary.model.Page
import com.leijendary.model.Page.Companion.empty
import com.leijendary.model.PageRequest
import com.leijendary.model.QueryRequest
import org.springframework.data.elasticsearch.client.elc.ElasticsearchTemplate
import org.springframework.data.elasticsearch.client.elc.NativeQueryBuilder
import org.springframework.data.elasticsearch.core.suggest.Completion
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.concurrent.atomic.AtomicInteger
import kotlin.streams.asSequence

private val SOURCE = ErrorSource(pointer = "/data/sampleDocument/id")
private const val STREAM_CHUNK = 1000

@Service
class SampleSearchService(
    private val elasticsearchTemplate: ElasticsearchTemplate,
    private val sampleDocumentRepository: SampleDocumentRepository,
    private val sampleRepository: SampleRepository,
) {
    fun page(queryRequest: QueryRequest, pageRequest: PageRequest): Page<SampleList> {
        if (queryRequest.query.isNullOrBlank()) {
            return empty(pageRequest)
        }

        val searchQuery = NativeQueryBuilder()
            .withQuery { query ->
                query.bool {
                    it.shouldMatch(queryRequest.query, "translations.name", "translations.description")
                }
            }
            .build()
        val searchHits = elasticsearchTemplate.search(searchQuery, SampleDocument::class.java)
        val list = searchHits.map { mapToList(it.content) }.toList()
        val total = searchHits.totalHits

        return Page(pageRequest, list, total)
    }

    fun save(sample: SampleDetail): SampleDocument {
        val document = map(sample)

        return sampleDocumentRepository.save(document)
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

    fun get(id: Long): SampleDetail {
        val document = sampleDocumentRepository.findByIdOrNull(id) ?: throw ResourceNotFoundException(id, SOURCE)

        return map(document)
    }

    fun update(sample: SampleDetail): SampleDocument {
        val document = sampleDocumentRepository.findByIdOrNull(sample.id)
            ?: throw ResourceNotFoundException(sample.id, SOURCE)
        document.update(sample)

        return sampleDocumentRepository.save(document)
    }

    fun delete(id: Long) {
        sampleDocumentRepository.deleteById(id)
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
