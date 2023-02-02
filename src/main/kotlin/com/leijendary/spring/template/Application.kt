package com.leijendary.spring.template

import com.leijendary.spring.template.core.config.properties.InfoProperties
import com.leijendary.spring.template.core.util.SpringContext.Companion.getBean
import org.springframework.boot.SpringBootVersion
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.core.env.get
import org.springframework.kafka.annotation.EnableKafka
import org.springframework.retry.annotation.EnableRetry
import org.springframework.scheduling.annotation.EnableAsync
import java.awt.SystemColor.info
import javax.swing.Spring

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
    runApplication<Application>(*args) {
        setBanner { environment, _, out ->
            val name = environment["info.app.name"]
            val version = environment["info.app.version"]
            val springVersion = SpringBootVersion.getVersion()

            out.println("Running $name v$version on Spring Boot v$springVersion")
        }
    }
}
