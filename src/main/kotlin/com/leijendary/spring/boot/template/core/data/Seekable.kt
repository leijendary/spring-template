package com.leijendary.spring.boot.template.core.data

import org.springframework.data.domain.Sort.Direction
import org.springframework.data.domain.Sort.Order.asc
import org.springframework.data.domain.Sort.Order.desc
import org.springframework.data.domain.Sort.by

class Seekable(
    val nextToken: String? = null,
    val limit: Int = 10,
    sortProperty: Array<String> = emptyArray(),
    sortDirection: String = "desc"
) {
    val direction = Direction.valueOf(sortDirection.uppercase())
    val sort = sortProperty
        .distinct()
        .map { if (direction.isAscending) asc(it) else desc(it) }
        .let { by(it) }
}
