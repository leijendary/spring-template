package com.leijendary.spring.api

import com.leijendary.spring.core.exception.ResourceNotFoundException
import com.leijendary.spring.core.exception.ResourceNotUniqueException
import com.leijendary.spring.core.extension.logger
import com.leijendary.spring.core.model.ErrorSource
import com.leijendary.spring.core.util.BeanContainer.jdbcTemplate
import com.leijendary.spring.core.util.Tracing
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal

data class SampleRequest(
    @field:NotBlank(message = "validation.required")
    val name: String? = null,

    val amount: BigDecimal? = null
)

@RestController
@RequestMapping("/api/v1/samples")
class SampleRest(private val kafkaTemplate: KafkaTemplate<String, String>) {
    private val log = logger()

    @PostMapping
    fun create(@Valid @RequestBody request: SampleRequest) {
        kafkaTemplate.send("sample.created", "TEST")
        jdbcTemplate.update(
            "insert into sample (name, amount, created_by, last_modified_by) values (?, ?, 'me', 'me')",
            request.name,
            request.amount
        )
    }

    @KafkaListener(topics = ["\${spring.kafka.topic.sampleCreated.name}"])
    @GetMapping("not-found")
    fun notFound() {
        throw ResourceNotFoundException(id = 1, source = ErrorSource(pointer = "/data/Sample/id"))
    }

    @GetMapping("not-unique")
    fun notUnique() {
        log.info("NOT UNIQUE")
        Tracing.log("f552bb235935c1875504fb8ddafcd881-d05d4b9cff4fca02") {
            log.info("TRACING HERE")
        }
        throw ResourceNotUniqueException(value = "code1", source = ErrorSource(pointer = "/data/Sample/code"))
    }
}
