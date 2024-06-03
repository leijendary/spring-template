package com.leijendary.domain.sample

import com.leijendary.extension.transactional
import com.leijendary.model.Page
import com.leijendary.model.PageRequest
import com.leijendary.model.QueryRequest
import com.leijendary.model.Seek
import com.leijendary.model.SeekRequest
import com.leijendary.util.requestContext
import org.springframework.stereotype.Service
import java.util.concurrent.CompletableFuture.supplyAsync

interface SampleService {
    fun page(queryRequest: QueryRequest, pageRequest: PageRequest): Page<SampleList>
    fun seek(queryRequest: QueryRequest, seekRequest: SeekRequest): Seek<SampleList>
    fun create(request: SampleRequest): SampleDetail
    fun get(id: Long, translate: Boolean): SampleDetail
    fun update(id: Long, version: Int, request: SampleRequest): SampleDetail
    fun delete(id: Long, version: Int)
}

@Service
class SampleServiceImpl(
    private val sampleMessageProducer: SampleMessageProducer,
    private val sampleRepository: SampleRepository
) : SampleService {
    override fun page(queryRequest: QueryRequest, pageRequest: PageRequest): Page<SampleList> {
        val samplesFuture = supplyAsync { sampleRepository.page(queryRequest, pageRequest) }
        val countFuture = supplyAsync { sampleRepository.count(queryRequest) }

        return Page(pageRequest, samplesFuture.get(), countFuture.get())
    }

    override fun seek(queryRequest: QueryRequest, seekRequest: SeekRequest): Seek<SampleList> {
        val samples = sampleRepository.seek(queryRequest, seekRequest)

        return Seek(seekRequest, samples)
    }

    override fun create(request: SampleRequest): SampleDetail {
        val sample = transactional {
            val sample = sampleRepository.create(request, requestContext.userIdOrSystem)
            val translations = sampleRepository.createTranslations(sample.id, request.translations)
            sample.translations.addAll(translations)
            sample
        }

        sampleMessageProducer.created(sample)

        return sample
    }

    override fun get(id: Long, translate: Boolean): SampleDetail {
        val sample = sampleRepository.get(id, translate)

        // Translation is already enabled, just return the translated record itself
        if (translate) {
            return sample
        }

        val translations = sampleRepository.listTranslations(sample.id)
        sample.translations.addAll(translations)

        return sample
    }

    override fun update(id: Long, version: Int, request: SampleRequest): SampleDetail {
        val sample = transactional {
            val sample = sampleRepository.update(id, version, request, requestContext.userIdOrSystem)
            val translations = sampleRepository.updateTranslations(id, request.translations)
            sample.translations.addAll(translations)
            sample
        }

        sampleMessageProducer.updated(sample)

        return sample
    }

    override fun delete(id: Long, version: Int) {
        sampleRepository.delete(id, version, requestContext.userIdOrSystem)
        sampleMessageProducer.deleted(id)
    }
}
