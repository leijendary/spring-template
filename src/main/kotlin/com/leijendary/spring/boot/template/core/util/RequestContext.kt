package com.leijendary.spring.boot.template.core.util

import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder.getContext
import org.springframework.web.context.request.RequestContextHolder.getRequestAttributes
import org.springframework.web.context.request.ServletRequestAttributes
import org.springframework.web.servlet.support.RequestContextUtils.getLocale
import org.springframework.web.servlet.support.RequestContextUtils.getTimeZone
import java.net.URI
import java.time.OffsetDateTime
import java.time.OffsetDateTime.now
import java.util.*
import javax.servlet.http.HttpServletRequest

object RequestContext {

    val currentRequest: HttpServletRequest?
        get() {
            val attributes = getRequestAttributes()

            return if (attributes !is ServletRequestAttributes) {
                null
            } else attributes.request
        }

    val username: String?
        get() {
            val context = getContext()

            return context.authentication?.let { obj: Authentication -> obj.name }
        }

    val path: String?
        get() {
            val request = currentRequest ?: return null
            val contextPath = request.contextPath

            return request.requestURI.replaceFirst(contextPath.toRegex(), "")
        }

    val uri: URI?
        get() {
            val request = currentRequest
            var path = path

            if (request == null || path == null) {
                return null
            }

            request.queryString?.also { path += "?$it" }

            return URI.create(path)
        }

    val timeZone: TimeZone
        get() = currentRequest?.let { getTimeZone(it) } ?: TimeZone.getDefault()

    val locale: Locale
        get() = currentRequest?.let { getLocale(it) } ?: Locale.getDefault()

    val language: String
        get() = locale.language

    val now: OffsetDateTime
        get() = now(timeZone.toZoneId())
}