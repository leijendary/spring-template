package com.leijendary.spring.boot.template.core.data

import org.springframework.data.domain.Page

class PageMeta(page: Page<*>) {

    private val numberOfElements: Int
    private val totalPages: Int
    private val totalElements: Long
    private val size: Int
    private val page: Int

    init {
        numberOfElements = page.numberOfElements
        totalPages = page.totalPages
        totalElements = page.totalElements
        size = page.size
        this.page = page.number + 1
    }
}