package com.leijendary.domain.sample

import com.leijendary.domain.image.ImageMultiValidateResponse
import com.leijendary.domain.sample.Sample.Companion.ENTITY
import com.leijendary.domain.sample.Sample.Companion.ERROR_SOURCE
import com.leijendary.error.exception.ResourceNotFoundException
import org.springframework.transaction.annotation.Transactional

@Transactional(readOnly = true)
fun <T> SampleRepository.findByIdOrThrow(id: String, type: Class<T>): T {
    return findById(id, type).orElseThrow { ResourceNotFoundException(id, ENTITY, ERROR_SOURCE) }
}

@Transactional(readOnly = true)
fun SampleRepository.findByIdOrThrow(id: String): Sample {
    return findById(id).orElseThrow { ResourceNotFoundException(id, ENTITY, ERROR_SOURCE) }
}

@Transactional(readOnly = true)
fun <T> SampleImageRepository.findByIdOrNull(id: String, type: Class<T>): T? {
    return findById(id, type).orElse(null)
}

fun Sample.toDetailResponse(translations: List<SampleTranslation>): SampleDetailResponse {
    return SampleDetailResponse(id, name, description, amount, version, createdAt).apply {
        this.translations.addAll(translations.toResponses())
    }
}

fun List<SampleTranslation>.toResponses() = map { it.toResponse() }

fun SampleTranslation.toResponse() = SampleTranslationResponse(name, description, language, ordinal)

fun SampleRequest.toEntity() = Sample(name, description, amount)

fun List<SampleTranslationRequest>.toEntities(id: String) = map { it.toEntity(id) }

fun SampleTranslationRequest.toEntity(id: String): SampleTranslation {
    return SampleTranslation(name, description, language, ordinal).apply { this.id = id }
}

fun SampleDetailResponse.applyTranslation(translation: SampleTranslation) {
    name = translation.name
    description = translation.description ?: description
}

fun Sample.updateWith(request: SampleRequest) {
    name = request.name
    description = request.description
    amount = request.amount
    version = request.version
}

fun ImageMultiValidateResponse.toSampleEntity(id: String): SampleImage {
    return SampleImage(original.name, preview.name, thumbnail.name).apply { this.id = id }
}
