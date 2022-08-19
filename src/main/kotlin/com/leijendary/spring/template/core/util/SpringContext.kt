package com.leijendary.spring.template.core.util

import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.stereotype.Component
import kotlin.reflect.KClass

@Component
class SpringContext : ApplicationContextAware {
    override fun setApplicationContext(applicationContext: ApplicationContext) {
        context = applicationContext
    }

    companion object {
        private var context: ApplicationContext? = null

        fun <T : Any> getBean(beanClass: KClass<T>): T {
            return context!!.getBean(beanClass.java)
        }

        fun isProd(): Boolean {
            return "prod" in context!!.environment.activeProfiles
        }
    }
}