package com.leijendary.spring.template.helper

import org.springframework.data.domain.Sort
import org.springframework.test.web.servlet.MockMvcResultMatchersDsl

object AssertionHelper {
    fun assertSeek(
        matchersDsl: MockMvcResultMatchersDsl,
        // Size of the content
        size: Int,
        // Requested page limit
        limit: Int,
        // Same as sort request
        sort: Sort,
        // Current expected page number
        page: Int,
        // Total number of pages
        totalPages: Int
    ) {
        with(matchersDsl) {
            jsonPath("$.nextToken") { if (page == totalPages) value(null) else isString() }
            jsonPath("$.size") { value(size) }
            jsonPath("$.limit") { value(limit) }
            jsonPath("$.sort") { isArray() }

            sort.forEachIndexed { index, order ->
                jsonPath("$.sort[$index].direction") { value(order.direction.toString()) }
                jsonPath("$.sort[$index].property") { value(order.property) }
                jsonPath("$.sort[$index].ascending") { value(order.isAscending) }
                jsonPath("$.sort[$index].descending") { value(order.isDescending) }
            }
        }
    }

    fun assertPage(
        matchersDsl: MockMvcResultMatchersDsl,
        // Requested size per page
        size: Int,
        // Same as sort request
        sort: Sort,
        // Current page
        page: Int,
        // Expected number of elements for this page
        numberOfElements: Int,
        // Total number of elements expected throughout all pages
        totalElements: Int,
        // Total number of pages
        totalPages: Int
    ) {
        with(matchersDsl) {
            assertSort(matchersDsl, "$.pageable.sort", sort)
            assertSort(matchersDsl, "$.sort", sort)

            jsonPath("$.pageable.offset") { value(page * size) }
            jsonPath("$.pageable.pageNumber") { value(page) }
            jsonPath("$.pageable.pageSize") { value(size) }
            jsonPath("$.pageable.paged") { value(true) }
            jsonPath("$.pageable.unpaged") { value(false) }
            jsonPath("$.last") { value(page + 1 == totalPages) }
            jsonPath("$.totalPages") { value(totalPages) }
            jsonPath("$.totalElements") { value(totalElements) }
            jsonPath("$.first") { value(page == 0) }
            jsonPath("$.size") { value(size) }
            jsonPath("$.number") { value(page) }
            jsonPath("$.numberOfElements") { value(numberOfElements) }

        }
    }

    fun assertSort(matchersDsl: MockMvcResultMatchersDsl, path: String, sort: Sort) {
        with(matchersDsl) {
            sort.onEachIndexed { index, order ->
                jsonPath("$path[$index].direction") { value(order.direction.toString()) }
                jsonPath("$path[$index].property") { value(order.property) }
                jsonPath("$path[$index].ascending") { value(order.isAscending) }
                jsonPath("$path[$index].descending") { value(order.isDescending) }
            }
        }
    }
}