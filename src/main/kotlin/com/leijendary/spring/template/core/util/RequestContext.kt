package com.leijendary.spring.template.core.util

import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.i18n.LocaleContextHolder.getLocale
import org.springframework.context.i18n.LocaleContextHolder.getTimeZone
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.context.request.RequestContextHolder.getRequestAttributes
import org.springframework.web.context.request.ServletRequestAttributes
import java.net.URI
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

const val HEADER_USER_ID = "X-User-ID"
const val HEADER_SCOPE = "X-Scope"

object RequestContext {
    val currentRequest: HttpServletRequest?
        get() {
            val attributes = getRequestAttributes()

            return if (attributes is ServletRequestAttributes) attributes.request else null
        }

    val userId: String
        get() = SecurityContextHolder.getContext().authentication.name

    val uri: URI?
        get() {
            val request = currentRequest ?: return null
            val contextPath = request.contextPath
            var url = request.requestURI.replaceFirst(contextPath.toRegex(), "")

            request.queryString?.also { url += "?$it" }

            return URI.create(url)
        }

    val timeZone: TimeZone
        get() = getTimeZone()

    val zoneId: ZoneId
        get() = timeZone.toZoneId()

    val locale: Locale
        get() = getLocale()

    val language: String
        get() = locale.language

    val now: LocalDateTime
        get() = LocalDateTime.now(zoneId)
}