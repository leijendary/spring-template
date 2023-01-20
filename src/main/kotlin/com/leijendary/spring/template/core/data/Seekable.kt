package com.leijendary.spring.template.core.data

import org.springframework.data.domain.Sort.Direction
import org.springframework.data.domain.Sort.Order.asc
import org.springframework.data.domain.Sort.Order.desc
import org.springframework.data.domain.Sort.by

class Seekable(
    val nextToken: String? = null,
    val limit: Int = 10,
    sort: Array<String> = emptyArray(),
    direction: String = "desc"
) : Response {
    val direction: Direction = Direction
        .fromOptionalString(direction.uppercase())
        .orElse(Direction.DESC)
    val sort = sort
        .distinct()
        .map { if (this.direction.isAscending) asc(it) else desc(it) }
        .let { by(it) }
}
