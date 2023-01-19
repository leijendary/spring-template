package com.leijendary.spring.template.core.data

class Seek<T>(val content: List<T>, val nextToken: String?, val size: Int, private val seekable: Seekable) {
    val limit: Int = seekable.limit
    val sortProperty: String = seekable.sort.map { it.property }.joinToString(",")
    val sortDirection: String = seekable.direction.name.lowercase()
    
    fun <R> map(transform: (T) -> R): Seek<R> {
        val content = content.map(transform)

        return Seek(content, nextToken, size, seekable)
    }
}
