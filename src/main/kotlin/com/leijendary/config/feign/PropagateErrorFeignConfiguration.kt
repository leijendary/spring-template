package com.leijendary.config.feign

import com.fasterxml.jackson.databind.ObjectMapper
import com.leijendary.extension.logger
import feign.codec.ErrorDecoder
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpStatus
import org.springframework.http.ProblemDetail
import org.springframework.web.ErrorResponseException
import java.nio.charset.StandardCharsets.UTF_8

class PropagateErrorFeignConfiguration(private val objectMapper: ObjectMapper) {
    private val log = logger()

    @Bean
    fun errorDecoder() = ErrorDecoder { methodKey, response ->
        val status = HttpStatus.valueOf(response.status())
        val problemDetail = response.body()
            .asReader(UTF_8)
            .use { objectMapper.readValue(it, ProblemDetail::class.java) }

        log.error("Feign error $status from $methodKey: $problemDetail")

        ErrorResponseException(status, problemDetail, null)
    }
}
