package com.leijendary.model

import com.fasterxml.jackson.annotation.JsonIgnore
import kotlin.math.max

@JvmRecord
data class PageRequest(val page: Int = 1, val size: Int = 20) {
    @JsonIgnore
    fun limit(): Int {
        return size
    }

    @JsonIgnore
    fun offset(): Int {
        return max(page.dec() * size, 0)
    }
}

data class Page<T>(private val request: PageRequest, val data: List<T>, val total: Long) {
    val page = request.page
    val size = request.size

    companion object {
        fun <T> empty(request: PageRequest): Page<T> {
            return Page(request, emptyList(), 0)
        }
    }
}
