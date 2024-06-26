package com.leijendary.config

import com.leijendary.config.properties.OpenApiProperties
import io.swagger.v3.oas.models.media.Schema
import org.springdoc.core.utils.SpringDocUtils
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME
import java.time.temporal.ChronoUnit.SECONDS

@Configuration(proxyBeanMethods = false)
@EnableConfigurationProperties(OpenApiProperties::class)
class OpenApiConfiguration {
    init {
        replaceLocalDateTime()
    }

    private fun replaceLocalDateTime() {
        val schema = Schema<LocalDateTime>()
            .type("string")
            .format("yyyyMMdd'T'HH:mm:ss")
            .example(LocalDateTime.now().truncatedTo(SECONDS).format(ISO_LOCAL_DATE_TIME))

        SpringDocUtils.getConfig().replaceWithSchema(LocalDateTime::class.java, schema)
    }
}
