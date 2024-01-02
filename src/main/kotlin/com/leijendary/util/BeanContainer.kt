package com.leijendary.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.leijendary.config.properties.AuthProperties
import com.leijendary.config.properties.NumberProperties
import com.leijendary.security.Encryption
import com.leijendary.storage.BlockStorage
import com.leijendary.util.SpringContext.Companion.getBean
import io.micrometer.tracing.Tracer
import org.springframework.transaction.PlatformTransactionManager

object BeanContainer {
    val authProperties by lazy { getBean(AuthProperties::class) }
    val blockStorage by lazy { getBean(BlockStorage::class) }
    val encryption by lazy { getBean(Encryption::class) }
    val numberProperties by lazy { getBean(NumberProperties::class) }
    val objectMapper by lazy { getBean(ObjectMapper::class) }
    val tracer by lazy { getBean(Tracer::class) }
    val transactionManager by lazy { getBean(PlatformTransactionManager::class) }
}
