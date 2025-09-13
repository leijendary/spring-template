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

data class CursoredModel<T : CursorProjection>(private val list: List<T>, private val cursorable: Cursorable) {
    val content = list.toMutableList()
    val metadata = CursorMetadata(cursorable.size)

    init {
        if (cursorable.size < content.size) {
            content.removeLast()

            val last = list.lastOrNull()

            metadata.apply {
                id = last?.id
                createdAt = last?.createdAt
            }
        }
    }
}

data class CursorMetadata(val size: Int, var createdAt: Instant? = null, var id: String? = null)
