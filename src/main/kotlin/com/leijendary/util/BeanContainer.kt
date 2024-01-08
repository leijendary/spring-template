package com.leijendary.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.leijendary.config.properties.AuthProperties
import com.leijendary.config.properties.NumberProperties
import com.leijendary.security.Encryption
import com.leijendary.storage.BlockStorage
import com.leijendary.util.SpringContext.Companion.getBean
import org.springframework.jdbc.support.JdbcTransactionManager

object BeanContainer {
    val authProperties by lazy { getBean(AuthProperties::class) }
    val blockStorage by lazy { getBean(BlockStorage::class) }
    val encryption by lazy { getBean(Encryption::class) }
    val numberProperties by lazy { getBean(NumberProperties::class) }
    val objectMapper by lazy { getBean(ObjectMapper::class) }
    val transactionManager by lazy { getBean(JdbcTransactionManager::class) }
}
