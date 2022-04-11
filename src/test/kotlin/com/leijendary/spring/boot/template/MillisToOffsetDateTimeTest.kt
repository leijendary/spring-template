package com.leijendary.spring.boot.template

import com.leijendary.spring.boot.template.core.extension.toClass
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.OffsetDateTime

class MillisToOffsetDateTimeTest : ApplicationTests() {
    @Test
    fun should_ConvertMillisToOffsetDateTimeProperly_Test() {
        val millis = 1649239708394
        val offsetDateTime = millis.toClass(OffsetDateTime::class.java)
        val string = offsetDateTime.toString()

        assertEquals("2022-04-06T18:08:28.394+08:00", string)
    }
}