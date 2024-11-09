package com.leijendary.domain.sample

import com.leijendary.context.DatabaseContext
import com.leijendary.context.RequestContext.language
import com.leijendary.domain.image.ImageMultiValidateResponse
import com.leijendary.domain.image.ImageRequest
import com.leijendary.domain.image.ImageResponse
import com.leijendary.domain.image.ImageService
import com.leijendary.domain.sample.Sample.Companion.ENTITY
import com.leijendary.domain.sample.Sample.Companion.ERROR_SOURCE
import com.leijendary.error.exception.ResourceNotFoundException
import com.leijendary.model.Cursorable
import com.leijendary.model.CursoredModel
import com.leijendary.model.QueryRequest
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

interface SampleService {
    fun page(queryRequest: QueryRequest, pageable: Pageable): Page<SampleResponse>
    fun cursor(queryRequest: QueryRequest, cursorable: Cursorable): CursoredModel<SampleResponse>
    fun create(request: SampleRequest): SampleDetailResponse
    fun get(id: String, translate: Boolean): SampleDetailResponse
    fun update(id: String, request: SampleRequest): SampleDetailResponse
    fun delete(id: String)
    fun saveImage(id: String, request: ImageRequest)
    fun deleteImage(id: String)
}

@Service
class SampleServiceImpl(
    private val databaseContext: DatabaseContext,
    private val imageService: ImageService,
    private val sampleImageRepository: SampleImageRepository,
    private val sampleMessageProducer: SampleMessageProducer,
    private val sampleRepository: SampleRepository,
    private val sampleSearchRepository: SampleSearchRepository,
    private val sampleTranslationRepository: SampleTranslationRepository
) : SampleService {
    override fun page(queryRequest: QueryRequest, pageable: Pageable): Page<SampleResponse> {
        return if (queryRequest.query !== null) {
            sampleRepository.findByNameContainingIgnoreCase(queryRequest.query, pageable, SampleResponse::class.java)
        } else {
            sampleRepository.findBy(pageable, SampleResponse::class.java)
        }
    }

    override fun cursor(queryRequest: QueryRequest, cursorable: Cursorable): CursoredModel<SampleResponse> {
        val samples = sampleRepository.cursor(queryRequest.query, cursorable, SampleResponse::class.java)

        return CursoredModel(samples, cursorable)
    }

    override fun create(request: SampleRequest): SampleDetailResponse {
        val response = databaseContext.transactional {
            val sample = sampleRepository.save(request.toEntity())
            val translations = sampleTranslationRepository.saveAll(request.translations.toEntities(sample.id))

            sample.toDetailResponse(translations)
        }

        sampleMessageProducer.created(response)

        return response
    }

    override fun get(id: String, translate: Boolean): SampleDetailResponse {
        val response = sampleRepository.findByIdOrThrow(id, SampleDetailResponse::class.java)
        val image = sampleImageRepository.findByIdOrNull(id, ImageResponse::class.java)
        response.image = image?.let(imageService::getPublicUrl)

        if (translate) {
            val translation = sampleTranslationRepository.findFirstByIdAndLanguage(id, language)
            translation?.let(response::applyTranslation)

            // Translation is already enabled, just return the translated record itself
            return response
        }

        val translations = sampleTranslationRepository.findById(id, SampleTranslationResponse::class.java)
        response.translations.addAll(translations)

        return response
    }

    override fun update(id: String, request: SampleRequest): SampleDetailResponse {
        val response = databaseContext.transactional {
            var sample = sampleRepository.findByIdOrThrow(id)
            sample.updateWith(request)
            sample = sampleRepository.save(sample)

            // Remove all translations from the database, and re-save them afterward.
            sampleTranslationRepository.deleteById(id)

            var translations = request.translations.toEntities(id)
            translations = sampleTranslationRepository.saveAll(translations)

            sample.toDetailResponse(translations)
        }
        val image = sampleImageRepository.findByIdOrNull(id, ImageResponse::class.java)
        response.image = image?.let(imageService::getPublicUrl)

        sampleMessageProducer.updated(response)

        return response
    }

    override fun delete(id: String) {
        sampleRepository.deleteById(id)
        sampleMessageProducer.deleted(id)
    }

    override fun saveImage(id: String, request: ImageRequest) {
        val exists = sampleRepository.existsById(id)

        if (!exists) {
            throw ResourceNotFoundException(id, ENTITY, ERROR_SOURCE)
        }

        val response = imageService.validate(request)
        val image = sampleImageRepository.findByIdOrNull(id) ?: response.toImage(id)

        sampleImageRepository.save(image)
        sampleSearchRepository.setImage(id, request)
    }

    override fun deleteImage(id: String) {
        sampleImageRepository.deleteById(id)
        sampleSearchRepository.deleteImage(id)
    }
}

fun ImageMultiValidateResponse.toImage(id: String): SampleImage {
    return SampleImage(original.name, preview.name, thumbnail.name).apply { this.id = id }
}
