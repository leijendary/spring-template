package com.leijendary.spring.core.error

import org.springframework.core.annotation.Order
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.client.HttpClientErrorException

@RestControllerAdvice
@Order(3)
class HttpClientErrorExceptionHandler {
    @ExceptionHandler(HttpClientErrorException::class)
    fun catchHttpClientError(exception: HttpClientErrorException): ResponseEntity<String> {
        return ResponseEntity
            .status(exception.statusCode)
            .headers(exception.responseHeaders)
            .body(exception.responseBodyAsString)
    }
}
