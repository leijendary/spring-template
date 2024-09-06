package com.leijendary.context

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component
import kotlin.properties.Delegates.notNull

@Component
class SpringContext(private val objectMapper: ObjectMapper) : ApplicationContextAware {
    init {
        Companion.objectMapper = objectMapper
    }

    override fun setApplicationContext(applicationContext: ApplicationContext) {
        isProd = applicationContext.environment.matchesProfiles("prod")
    }

    companion object {
        var isProd by notNull<Boolean>()
        lateinit var objectMapper: ObjectMapper
    }
}
