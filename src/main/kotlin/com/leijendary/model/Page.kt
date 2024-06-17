package com.leijendary.model

import com.fasterxml.jackson.annotation.JsonIgnore
import org.springframework.data.domain.Sort.Direction
import org.springframework.data.domain.Sort.Direction.DESC
import org.springframework.data.domain.PageRequest as Pageable

data class PageRequest(
    val page: Int = 1,
    val size: Int = 20,
    val direction: Direction = DESC,
    val sort: List<String> = emptyList()
) {
    @get:JsonIgnore
    val limit: Int
        get() = size

    @get:JsonIgnore
    val offset: Int
        get() = page.dec() * size

    @get:JsonIgnore
    val pageable: Pageable
        get() {
            if (sort.isEmpty()) {
                return Pageable.of(page.dec(), size)
            }

            return Pageable.of(page.dec(), size, direction, *sort.toTypedArray())
        }
}

data class Page<T>(private val request: PageRequest, val data: List<T>, val total: Long) {
    val page = request.page
    val size = request.size
    val direction = request.direction
    val sort = request.sort

    fun <R> map(mapper: (T) -> R): Page<R> {
        val mapped = data.map(mapper)

        return Page(request, mapped, total)
    }

    companion object {
        fun <T> empty(request: PageRequest): Page<T> {
            return Page(request, emptyList(), 0)
        }
    }
}
