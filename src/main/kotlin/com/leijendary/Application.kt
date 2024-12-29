package com.leijendary

import com.leijendary.model.IdentityModel
import com.leijendary.validator.UniqueFieldsValidator
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn.HEADER
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar
import org.springframework.aot.hint.annotation.RegisterReflectionForBinding
import org.springframework.boot.SpringBootVersion
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.ImportRuntimeHints
import org.springframework.core.env.get
import org.springframework.data.web.PagedModel
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.retry.annotation.EnableRetry
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication(proxyBeanMethods = false)
@ImportRuntimeHints(ApplicationRuntimeHints::class)
@RegisterReflectionForBinding(value = [PagedModel::class, UniqueFieldsValidator::class])
@SecurityScheme(name = AUTHORIZATION, type = HTTP, `in` = HEADER, scheme = "bearer", bearerFormat = "JWT")
@EnableAsync
@EnableFeignClients
@EnableRetry
class Application

class ApplicationRuntimeHints : RuntimeHintsRegistrar {
    override fun registerHints(hints: RuntimeHints, classLoader: ClassLoader?) {
        val resources = arrayOf(
            "messages/*",
            "prompts/*",
            "io/awspring/cloud/core/SpringCloudClientConfiguration.properties",
            "io/awspring/cloud/s3/S3ObjectContentTypeResolver.properties"
        )
        // Classes to be registered for serialization
        val serializations = arrayOf(IdentityModel::class.java)

        // Resources
        resources.forEach(hints.resources()::registerPattern)

        // Serialization
        serializations.forEach(hints.serialization()::registerType)
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args) {
        setBanner { environment, _, out ->
            val name = environment["openApi.info.title"]
            val version = environment["openApi.info.version"]
            val springVersion = SpringBootVersion.getVersion()
            val kotlinVersion = KotlinVersion.CURRENT

            out.print("Running $name v$version on Spring Boot v$springVersion and Kotlin v$kotlinVersion")
        }
    }
}
