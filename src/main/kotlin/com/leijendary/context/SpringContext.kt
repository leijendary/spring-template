package com.leijendary.context

import com.fasterxml.jackson.databind.ObjectMapper
import com.leijendary.config.DatabaseConfiguration.Companion.BEAN_READ_ONLY_TRANSACTION_TEMPLATE
import com.leijendary.context.SpringContext.Companion.getBean
import com.leijendary.security.Encryption
import com.leijendary.storage.BlockStorage
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationContextAware
import org.springframework.core.env.Profiles
import org.springframework.stereotype.Component
import org.springframework.transaction.support.TransactionTemplate
import kotlin.properties.Delegates.notNull
import kotlin.reflect.KClass

val blockStorage by lazy { getBean(BlockStorage::class) }
val encryption by lazy { getBean(Encryption::class) }
val objectMapper by lazy { getBean(ObjectMapper::class) }
val readOnlyTransactionalTemplate by lazy { getBean(BEAN_READ_ONLY_TRANSACTION_TEMPLATE, TransactionTemplate::class) }
val requestContext by lazy { getBean(RequestContext::class) }
val transactionTemplate by lazy { getBean(TransactionTemplate::class) }

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
