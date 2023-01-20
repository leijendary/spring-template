package com.leijendary.spring.template

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.Sort
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.MockMvcResultMatchersDsl

@SpringBootTest
@ExtendWith(SpringExtension::class)
@AutoConfigureMockMvc
class ApplicationTest {
    @Autowired
    protected lateinit var mockMvc: MockMvc

    @Test
    fun contextLoads() {
    }

    protected fun assertSeek(
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

    protected fun assertPage(
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

    protected fun assertSort(matchersDsl: MockMvcResultMatchersDsl, path: String, sort: Sort) {
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
