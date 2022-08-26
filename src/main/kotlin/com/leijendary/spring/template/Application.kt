package com.leijendary.spring.template

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.retry.annotation.EnableRetry
import org.springframework.scheduling.annotation.EnableAsync

@EnableAsync
@EnableCaching
@EnableDiscoveryClient
@EnableFeignClients
@EnableKafka
@EnableRetry
@SpringBootApplication(
    exclude = [
        ErrorMvcAutoConfiguration::class,
        UserDetailsServiceAutoConfiguration::class,
    ]
)
class Application

fun main(args: Array<String>) {
    runApplication<Application>(*args)
}
