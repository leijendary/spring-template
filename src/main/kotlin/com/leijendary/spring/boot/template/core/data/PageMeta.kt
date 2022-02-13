package com.leijendary.spring.boot.template.core.data

import org.springframework.data.domain.Page

class PageMeta(page: Page<*>) {

    var numberOfElements: Int = page.numberOfElements
    var totalPages: Int = page.totalPages
    var totalElements: Long = page.totalElements
    var size: Int = page.size
    var number: Int = page.number
}