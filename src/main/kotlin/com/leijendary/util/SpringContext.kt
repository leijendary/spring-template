package com.leijendary.util

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.core.env.Profiles
import org.springframework.stereotype.Component
import kotlin.properties.Delegates.notNull
import kotlin.reflect.KClass

@Component
class SpringContext : ApplicationContextAware {
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        context = applicationContext
        isProd = context.environment.acceptsProfiles(Profiles.of("prod"))
    }

    companion object {
        var isProd by notNull<Boolean>()

        private lateinit var context: ApplicationContext

        fun <T : Any> getBean(beanClass: KClass<T>): T {
            return context.getBean(beanClass.java)
        }

        fun <T : Any> getBean(name: String, beanClass: KClass<T>): T {
            return context.getBean(name, beanClass.java)
        }
    }
}
