package com.leijendary

import com.leijendary.model.IdentityModel
import com.leijendary.validator.UniqueFieldsValidator
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn.HEADER
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP
import io.swagger.v3.oas.annotations.security.SecurityScheme
import liquibase.changelog.ChangeLogHistoryServiceFactory
import org.apache.kafka.clients.consumer.CooperativeStickyAssignor
import org.apache.kafka.common.security.authenticator.AbstractLogin.DefaultLoginCallbackHandler
import org.apache.kafka.common.security.authenticator.DefaultLogin
import org.apache.kafka.common.security.authenticator.SaslClientAuthenticator
import org.apache.kafka.common.security.authenticator.SaslClientCallbackHandler
import org.apache.kafka.common.security.scram.ScramLoginModule
import org.apache.kafka.common.security.scram.internals.ScramFormatter
import org.apache.kafka.common.security.scram.internals.ScramSaslClient
import org.apache.kafka.common.security.scram.internals.ScramSaslClient.ScramSaslClientFactory
import org.bouncycastle.jcajce.provider.asymmetric.RSA.Mappings
import org.bouncycastle.jcajce.provider.asymmetric.rsa.KeyFactorySpi
import org.springframework.aot.hint.ExecutableMode.INVOKE
import org.springframework.aot.hint.MemberCategory.*
import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar
import org.springframework.aot.hint.registerType
import org.springframework.boot.SpringBootVersion
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.ImportRuntimeHints
import org.springframework.core.env.get
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.retry.annotation.EnableRetry
import org.springframework.scheduling.annotation.EnableAsync
import javax.security.auth.spi.LoginModule
import javax.security.sasl.SaslClient

@EnableAsync
@EnableFeignClients
@EnableRetry
@ImportRuntimeHints(ApplicationRuntimeHints::class)
@SecurityScheme(name = AUTHORIZATION, type = HTTP, `in` = HEADER, scheme = "bearer", bearerFormat = "JWT")
@SpringBootApplication(proxyBeanMethods = false)
class Application

class ApplicationRuntimeHints : RuntimeHintsRegistrar {
    override fun registerHints(hints: RuntimeHints, classLoader: ClassLoader?) {
        val categories = listOf(INVOKE_DECLARED_CONSTRUCTORS, INVOKE_DECLARED_METHODS, DECLARED_CLASSES)

        // Reflection
        hints.reflection()
            .registerType<ChangeLogHistoryServiceFactory> { it.withConstructor(emptyList(), INVOKE) }
            .registerType<CooperativeStickyAssignor>(*categories.toTypedArray())
            .registerType<DefaultLogin>(*categories.toTypedArray())
            .registerType<DefaultLoginCallbackHandler>(*categories.toTypedArray())
            .registerType<KeyFactorySpi>(*categories.toTypedArray())
            .registerType<LoginModule>(*categories.toTypedArray())
            .registerType<Mappings>(*categories.toTypedArray())
            .registerType<SaslClient>(*categories.toTypedArray())
            .registerType<SaslClientAuthenticator>(*categories.toTypedArray())
            .registerType<SaslClientCallbackHandler>(*categories.toTypedArray())
            .registerType<ScramFormatter>(*categories.toTypedArray())
            .registerType<ScramLoginModule>(*categories.toTypedArray())
            .registerType<ScramSaslClient>(*categories.toTypedArray())
            .registerType<ScramSaslClientFactory>(*categories.toTypedArray())
            .registerType<UniqueFieldsValidator> { it.withConstructor(emptyList(), INVOKE) }

        // Resources
        hints.resources()
            .registerPattern("elasticsearch/settings.json")
            .registerPattern("messages/*")
            .registerPattern("io/awspring/cloud/core/SpringCloudClientConfiguration.properties")
            .registerPattern("io/awspring/cloud/s3/S3ObjectContentTypeResolver.properties")

        // Serialization
        hints.serialization()
            .registerType(IdentityModel::class.java)
    }
}

fun main(args: Array<String>) {
    runApplication<Application>(*args) {
        setBanner { environment, _, out ->
            val name = environment["openApi.info.title"]
            val version = environment["openApi.info.version"]
            val springVersion = SpringBootVersion.getVersion()

            out.print("Running $name v$version on Spring Boot v$springVersion")
        }
    }
}
