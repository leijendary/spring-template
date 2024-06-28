package com.leijendary.extension

import org.springframework.data.redis.core.RedisOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.core.SessionCallback

fun <K, V, T> RedisTemplate<K, V>.transactional(action: (RedisOperations<K, V>) -> T): T {
    val callback = object : SessionCallback<T> {
        override fun <KK, VV> execute(operations: RedisOperations<KK, VV>): T = try {
            @Suppress("UNCHECKED_CAST")
            action(operations as RedisOperations<K, V>)
        } catch (e: Exception) {
            operations.discard()

            throw e
        }
    }

    return execute(callback)
}
