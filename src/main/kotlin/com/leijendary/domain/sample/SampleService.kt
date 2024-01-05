package com.leijendary.domain.sample

import com.leijendary.extension.transactional
import com.leijendary.model.*
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture.supplyAsync

@Service
class SampleService(
    private val sampleMessageProducer: SampleMessageProducer,
    private val sampleRepository: SampleRepository
) {
    fun page(queryRequest: QueryRequest, pageRequest: PageRequest): Page<SampleList> {
        val samplesFuture = supplyAsync { sampleRepository.page(queryRequest, pageRequest) }
        val countFuture = supplyAsync { sampleRepository.count(queryRequest) }

        return Page(pageRequest, samplesFuture.get(), countFuture.get())
    }

    fun seek(queryRequest: QueryRequest, seekRequest: SeekRequest): Seek<SampleList> {
        val samples = sampleRepository.seek(queryRequest, seekRequest)

        return Seek(seekRequest, samples)
    }

    fun create(request: SampleRequest): SampleDetail {
        val sample = transactional {
            val sample = sampleRepository.create(request)
            val translations = sampleRepository.createTranslations(sample.id, request.translations)
            sample.translations.addAll(translations)

            sample
        }

        sampleMessageProducer.created(sample)

        return sample
    }

    fun get(id: Long, translate: Boolean): SampleDetail {
        val sample = sampleRepository.get(id, translate)

        // Translation is already enabled, just return the translated record itself
        if (translate) {
            return sample
        }

        val translations = sampleRepository.listTranslations(sample.id)
        sample.translations.addAll(translations)

        return sample
    }

    fun update(id: Long, version: Int, request: SampleRequest): SampleDetail {
        val sample = transactional {
            val sample = sampleRepository.update(id, version, request)
            val translations = sampleRepository.updateTranslations(id, request.translations)
            sample.translations.addAll(translations)

            sample
        }

        sampleMessageProducer.updated(sample)

        return sample
    }

    fun delete(id: Long, version: Int) {
        sampleRepository.delete(id, version)
        sampleMessageProducer.deleted(id)
    }
}
