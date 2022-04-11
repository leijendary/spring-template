package com.leijendary.spring.boot.template.core.data

class Seek<T>(val content: List<T>, val nextToken: String?, val size: Int, val seekable: Seekable) {
    fun <R> map(transform: (T) -> R): Seek<R> {
        val content = content.map(transform)

        return Seek(content, nextToken, size, seekable)
    }
}