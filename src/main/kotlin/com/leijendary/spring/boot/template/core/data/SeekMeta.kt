package com.leijendary.spring.boot.template.core.data

class SeekMeta(seek: Seek<*>) {
    val size: Int = seek.size
    val limit: Int = seek.seekable.limit
    val sortProperty: String = seek.seekable.sort.map { it.property }.joinToString(",")
    val sortDirection: String = seek.seekable.direction.name.lowercase()
    val nextToken: String? = seek.nextToken
}