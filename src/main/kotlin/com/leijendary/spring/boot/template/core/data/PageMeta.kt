package com.leijendary.spring.boot.template.core.data

import org.springframework.data.domain.Page

class PageMeta(page: Page<*>) {
    val numberOfElements: Int = page.numberOfElements
    val totalPages: Int = page.totalPages
    val totalElements: Long = page.totalElements
    val size: Int = page.size
    val number: Int = page.number
}