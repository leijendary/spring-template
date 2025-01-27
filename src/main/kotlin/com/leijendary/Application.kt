package com.leijendary

import org.springframework.boot.SpringBootVersion
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.ImportRuntimeHints
import org.springframework.core.env.get
import org.springframework.retry.annotation.EnableRetry
import org.springframework.scheduling.annotation.EnableAsync

@SpringBootApplication(proxyBeanMethods = false)
@ImportRuntimeHints(ApplicationRuntimeHints::class)
@EnableAsync
@EnableFeignClients
@EnableRetry
class Application

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
