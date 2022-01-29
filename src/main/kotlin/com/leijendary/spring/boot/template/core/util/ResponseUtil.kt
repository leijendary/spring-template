package com.leijendary.spring.boot.template.core.util

import org.springframework.http.HttpHeaders.LOCATION
import java.util.*
import javax.servlet.http.HttpServletResponse

fun HttpServletResponse.setLocation(id: UUID?) {
    this.setHeader(LOCATION, id!!.toLocation())
}