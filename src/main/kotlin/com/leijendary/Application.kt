package com.leijendary

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn.HEADER
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType.HTTP
import io.swagger.v3.oas.annotations.security.SecurityScheme
import org.springframework.boot.SpringBootVersion
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.core.env.get
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.retry.annotation.EnableRetry
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@EnableDiscoveryClient
@EnableFeignClients
@EnableRetry
@SecurityScheme(name = AUTHORIZATION, type = HTTP, `in` = HEADER, scheme = "bearer")
@SpringBootApplication
class Application

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
