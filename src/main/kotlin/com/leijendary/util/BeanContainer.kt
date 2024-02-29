package com.leijendary.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.leijendary.security.Encryption
import com.leijendary.storage.BlockStorage
import com.leijendary.util.SpringContext.Companion.getBean
import org.springframework.jdbc.support.JdbcTransactionManager

val blockStorage by lazy { getBean(BlockStorage::class) }
val encryption by lazy { getBean(Encryption::class) }
val objectMapper by lazy { getBean(ObjectMapper::class) }
val transactionManager by lazy { getBean(JdbcTransactionManager::class) }
