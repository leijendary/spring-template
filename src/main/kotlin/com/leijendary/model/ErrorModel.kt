package com.leijendary.model

import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import org.springframework.core.annotation.AliasFor
import org.springframework.http.ProblemDetail

@ApiResponse(content = [Content(schema = Schema(implementation = ProblemDetail::class))])
annotation class ErrorModelsResponse(
    @get:AliasFor(annotation = ApiResponse::class, attribute = "responseCode")
    val status: String,

    @get:AliasFor(annotation = ApiResponse::class, attribute = "description")
    val description: String,
)

data class ErrorModel(val code: String, val message: String? = null, val pointer: String, val id: Any? = null)
