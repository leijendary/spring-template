package com.leijendary.spring.core.model

import org.springframework.data.domain.Sort

class Seek<T>(private val content: List<T>, val nextToken: String?, val size: Int, val limit: Int, val sort: Sort) {
    fun <R> map(transform: (T) -> R): Seek<R> {
        val content = content.map(transform)

        return Seek(content, nextToken, size, limit, sort)
    }
}
