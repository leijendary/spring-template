package com.leijendary.spring.core.model

import org.springframework.data.domain.Sort.Direction
import org.springframework.data.domain.Sort.Direction.DESC
import java.time.OffsetDateTime

data class SeekRequest(val nextToken: String? = null, val limit: Int = 20, val direction: Direction = DESC)

data class SeekNextToken(val createdAt: OffsetDateTime, val id: Long)

data class Seek<T>(
    val content: List<T>,
    val nextToken: String?,
    val limit: Int,
    val size: Int,
    val isLast: Boolean
) {
    fun <R> map(transform: (T) -> R): Seek<R> {
        val content = content.map(transform)

        return Seek(content, nextToken, limit, size, isLast)
    }
}
