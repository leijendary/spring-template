package com.leijendary.context

import com.fasterxml.jackson.databind.ObjectMapper
import jakarta.annotation.PostConstruct
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.core.env.Profiles
import org.springframework.stereotype.Component
import kotlin.properties.Delegates.notNull

@Component
class SpringContext(private val objectMapper: ObjectMapper) : ApplicationContextAware {
    @PostConstruct
    fun init() {
        SpringContext.objectMapper = objectMapper
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        isProd = applicationContext.environment.acceptsProfiles(Profiles.of("prod"))
    }

    companion object {
        var isProd by notNull<Boolean>()
        lateinit var objectMapper: ObjectMapper
    }
}
