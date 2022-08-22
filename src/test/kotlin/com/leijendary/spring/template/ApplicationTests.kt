package com.leijendary.spring.template

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment.MOCK
import org.springframework.test.context.junit.jupiter.SpringExtension

@SpringBootTest(webEnvironment = MOCK)
@ExtendWith(SpringExtension::class)
@AutoConfigureMockMvc
class ApplicationTests {
    @Test
    fun contextLoads() {
    }
}
