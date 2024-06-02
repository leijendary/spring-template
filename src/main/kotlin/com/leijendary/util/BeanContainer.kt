package com.leijendary.util

import com.fasterxml.jackson.databind.ObjectMapper
import com.leijendary.config.DatabaseConfiguration.Companion.BEAN_READ_ONLY_TRANSACTION_TEMPLATE
import com.leijendary.security.Encryption
import com.leijendary.storage.BlockStorage
import com.leijendary.util.SpringContext.Companion.getBean
import org.springframework.transaction.support.TransactionTemplate

val blockStorage by lazy { getBean(BlockStorage::class) }
val encryption by lazy { getBean(Encryption::class) }
val objectMapper by lazy { getBean(ObjectMapper::class) }
val readOnlyTransactionalTemplate by lazy { getBean(BEAN_READ_ONLY_TRANSACTION_TEMPLATE, TransactionTemplate::class) }
val requestContext by lazy { getBean(RequestContext::class) }
val transactionTemplate by lazy { getBean(TransactionTemplate::class) }
