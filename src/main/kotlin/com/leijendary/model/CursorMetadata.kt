package com.leijendary.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.leijendary.projection.CursorProjection
import java.sql.Timestamp
import java.time.Instant

data class Cursorable(val size: Int = 20, val createdAt: Instant? = null, val id: String? = null) {
    @get:JsonIgnore
    val limit: Int = size.inc()

    // Used for queries from the database
    @get:JsonIgnore
    val timestamp = createdAt?.let(Timestamp::from)
}

data class CursoredModel<T : CursorProjection>(val content: MutableList<T>, private val cursorable: Cursorable) {
    val cursor = CursorMetadata(cursorable.size)

    init {
        if (content.size > cursorable.size) {
            content.removeLast()

            val last = content.lastOrNull()
            cursor.apply {
                id = last?.id
                createdAt = last?.createdAt
            }
        }
    }
}

data class CursorMetadata(val size: Int, var createdAt: Instant? = null, var id: String? = null)
