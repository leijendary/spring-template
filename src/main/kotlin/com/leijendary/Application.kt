package com.leijendary

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn.HEADER
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP
import io.swagger.v3.oas.annotations.security.SecurityScheme
import liquibase.changelog.ChangeLogHistoryServiceFactory
import org.bouncycastle.jcajce.provider.asymmetric.RSA
import org.bouncycastle.jcajce.provider.asymmetric.rsa.KeyFactorySpi
import org.springframework.aot.hint.ExecutableMode.INVOKE
import org.springframework.aot.hint.MemberCategory.INVOKE_PUBLIC_CONSTRUCTORS
import org.springframework.aot.hint.MemberCategory.INVOKE_PUBLIC_METHODS
import org.springframework.aot.hint.RuntimeHints
import org.springframework.aot.hint.RuntimeHintsRegistrar
import org.springframework.boot.SpringBootVersion
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.ImportRuntimeHints
import org.springframework.core.env.get
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.retry.annotation.EnableRetry
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@EnableDiscoveryClient
@EnableFeignClients
@EnableRetry
@ImportRuntimeHints(ApplicationRuntimeHints::class)
@SecurityScheme(name = AUTHORIZATION, type = HTTP, `in` = HEADER, scheme = "bearer", bearerFormat = "JWT")
@SpringBootApplication
class Application

class ApplicationRuntimeHints : RuntimeHintsRegistrar {
    override fun registerHints(hints: RuntimeHints, classLoader: ClassLoader?) {
        // Reflection
        hints.reflection()
            .registerType(ChangeLogHistoryServiceFactory::class.java) {
                it.withConstructor(emptyList(), INVOKE)
            }
            .registerType(KeyFactorySpi::class.java, INVOKE_PUBLIC_CONSTRUCTORS, INVOKE_PUBLIC_METHODS)
            .registerType(RSA.Mappings::class.java, INVOKE_PUBLIC_CONSTRUCTORS, INVOKE_PUBLIC_METHODS)

        // Resources
        hints.resources().registerPattern("*")
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
