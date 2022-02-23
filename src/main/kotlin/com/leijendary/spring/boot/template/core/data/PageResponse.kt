package com.leijendary.spring.boot.template.core.data

import org.springframework.data.domain.Page

class PageResponse<T>(page: Page<T>) {
    var content: List<T> = page.content
    var number: Int = page.number
    var size: Int = page.size
    var totalPages: Int = page.totalPages
    var numberOfElements: Int = page.numberOfElements
    var totalElements: Long = page.totalElements
    var previous: Boolean = page.hasPrevious()
    var first: Boolean = page.isFirst
    var next: Boolean = page.hasNext()
    var last: Boolean = page.isLast
}