package com.leijendary.spring.core.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.leijendary.spring.core.config.properties.AuthProperties
import com.leijendary.spring.core.config.properties.NumberProperties
import com.leijendary.spring.core.storage.BlockStorage
import com.leijendary.spring.core.util.SpringContext.Companion.getBean
import io.micrometer.tracing.Tracer
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.transaction.PlatformTransactionManager

object BeanContainer {
    val authProperties by lazy { getBean(AuthProperties::class) }
    val blockStorage by lazy { getBean(BlockStorage::class) }
    val numberProperties by lazy { getBean(NumberProperties::class) }
    val objectMapper by lazy { getBean(ObjectMapper::class) }
    val tracer by lazy { getBean(Tracer::class) }
    val jdbcTemplate by lazy { getBean(JdbcTemplate::class) }
    val transactionManager by lazy { getBean(PlatformTransactionManager::class) }
}
