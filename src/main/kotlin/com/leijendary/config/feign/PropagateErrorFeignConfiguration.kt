package com.leijendary.config.feign

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.leijendary.model.ErrorModel
import com.leijendary.error.exception.ErrorModelException
import com.leijendary.extension.logger
import feign.codec.ErrorDecoder
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import java.nio.charset.StandardCharsets.UTF_8

class PropagateErrorFeignConfiguration(private val objectMapper: ObjectMapper) {
    private val log = logger()

    @Bean
    fun errorDecoder() = ErrorDecoder { methodKey, response ->
        val status = HttpStatus.valueOf(response.status())
        val errors = response.body().asReader(UTF_8).use {
            objectMapper.readValue(it, object : TypeReference<List<ErrorModel>>() {})
        }

        log.error("Feign error $status from $methodKey: $errors")

        ErrorModelException(status, errors)
    }
}
