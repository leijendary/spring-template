package com.leijendary.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.leijendary.projection.SeekProjection
import java.time.OffsetDateTime

data class SeekRequest(val size: Int = 20, val createdAt: OffsetDateTime? = null, val id: Long? = null) {
    @JsonIgnore
    fun limit(): Int {
        return size.inc()
    }
}

data class Seek<T : SeekProjection>(private val request: SeekRequest, val data: MutableList<T>) {
    val size = request.size
    var createdAt: OffsetDateTime? = null
    var id: Long? = null

    init {
        if (data.size > size) {
            data.removeLast()
            data.lastOrNull()?.let {
                this.createdAt = it.createdAt
                this.id = it.id
            }
        }
    }
}
