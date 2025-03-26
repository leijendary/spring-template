package com.leijendary.model

import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.core.annotation.AliasFor

@ApiResponse(content = [Content(array = ArraySchema(schema = Schema(implementation = ErrorModel::class)))])
annotation class ErrorModelsResponse(
    @get:AliasFor(annotation = ApiResponse::class, attribute = "responseCode")
    val status: String,

    @get:AliasFor(annotation = ApiResponse::class, attribute = "description")
    val description: String,
)

data class ErrorModel(val id: Any? = null, val code: String, val message: String? = null, val source: ErrorSource)

data class ErrorSource(
    val pointer: String? = null,
    val parameter: String? = null,
    val header: String? = null,
    val meta: MutableMap<String, Any>? = null
)
