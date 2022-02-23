package com.leijendary.spring.boot.template.core.util

import org.springframework.context.i18n.LocaleContextHolder.getLocale
import org.springframework.context.i18n.LocaleContextHolder.getTimeZone
import org.springframework.web.context.request.RequestContextHolder.getRequestAttributes
import org.springframework.web.context.request.ServletRequestAttributes
import java.net.URI
import java.time.OffsetDateTime
import java.time.OffsetDateTime.now
import java.util.*
import javax.servlet.http.HttpServletRequest

const val HEADER_USER_ID = "X-User-ID"

object RequestContext {
    val currentRequest: HttpServletRequest?
        get() {
            val attributes = getRequestAttributes()

            return if (attributes !is ServletRequestAttributes) null else attributes.request
        }

    val username: String?
        get() = currentRequest?.getHeader(HEADER_USER_ID)

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
        get() = getTimeZone()

    val locale: Locale
        get() = getLocale()

    val language: String
        get() = locale.language

    val now: OffsetDateTime
        get() = now(timeZone.toZoneId())
}