package com.leijendary.context

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.context.MessageSource
import org.springframework.stereotype.Component
import kotlin.properties.Delegates.notNull

@Component
class SpringContext : ApplicationContextAware {
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        isProd = applicationContext.environment.matchesProfiles("prod")
        messageSource = applicationContext.getBean(MessageSource::class.java)
        objectMapper = applicationContext.getBean(ObjectMapper::class.java)
    }

    companion object {
        var isProd by notNull<Boolean>()
        lateinit var messageSource: MessageSource
        lateinit var objectMapper: ObjectMapper
    }
}
