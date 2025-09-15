package com.leijendary.domain.sample

import com.leijendary.model.ImageProjection
import io.mcarle.konvert.api.Konvert
import io.mcarle.konvert.api.Konverter
import io.mcarle.konvert.api.Mapping
import org.springframework.data.elasticsearch.core.suggest.Completion

@Konverter
interface SampleMapper {
    @Konvert(
        mappings = [
            Mapping(target = "translations", ignore = true),
            Mapping(target = "version", ignore = true),
        ]
    )
    fun toEntity(request: SampleRequest): Sample

    fun toEntity(request: SampleTranslationRequest): SampleTranslation
    fun toResponse(sample: Sample): SampleResponse
    fun toDetailResponse(sample: Sample): SampleDetailResponse
    fun toResponse(translation: SampleTranslation): SampleTranslationResponse
    fun toMessage(sample: Sample): SampleMessage
    fun toMessage(translation: SampleTranslation): SampleTranslationMessage
    fun toResponse(cursor: SampleCursor): SampleResponse

    @Konvert(
        mappings = [
            Mapping("completion", expression = "message.translations.map { it.name }.let(::toCompletion)"),
        ]
    )
    fun toSearch(message: SampleMessage): SampleSearch

    fun toSearch(message: SampleTranslationMessage): SampleTranslationSearch

    @Konvert(
        mappings = [
            Mapping("completion", expression = "response.translations.map { it.name }.let(::toCompletion)"),
        ]
    )
    fun toSearch(response: SampleDetailResponse): SampleSearch

    fun toSearch(response: SampleTranslationResponse): SampleTranslationSearch
    fun toCompletion(names: List<String>) = Completion(names)

    fun toResponse(search: SampleSearch, language: String, image: ImageProjection?): SampleResponse {
        val translation = search.getTranslationOrFirst(language)
        val response = SampleResponse(
            id = search.id,
            name = translation.name,
            description = translation.description,
            amount = search.amount,
            createdAt = search.createdAt,
        )
        response.image = image

        return response
    }

    fun applyTranslation(response: SampleDetailResponse, entity: SampleTranslation) {
        response.name = entity.name
        response.description = entity.description ?: response.description
    }

    fun update(request: SampleRequest, sample: Sample) {
        sample.apply {
            name = request.name
            description = request.description
            amount = request.amount
            version = request.version
        }
    }
}
