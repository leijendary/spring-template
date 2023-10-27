package com.leijendary.spring.core.util

import com.leijendary.spring.core.exception.StatusException
import com.leijendary.spring.core.model.ErrorSource
import com.leijendary.spring.core.util.BeanContainer.authProperties
import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.i18n.LocaleContextHolder.getLocale
import org.springframework.context.i18n.LocaleContextHolder.getTimeZone
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.web.context.request.RequestAttributes.SCOPE_REQUEST
import org.springframework.web.context.request.RequestContextHolder.getRequestAttributes
import org.springframework.web.context.request.ServletRequestAttributes
import java.time.OffsetDateTime
import java.time.ZoneId
import java.util.*

const val HEADER_USER_ID = "X-User-ID"

private val SESSION_NO_FOUND_EXCEPTION = StatusException(
    code = "access.session.notFound",
    status = UNAUTHORIZED,
    source = ErrorSource(header = HEADER_USER_ID)
)

object RequestContext {
    val currentRequest: HttpServletRequest?
        get() = getRequestAttributes()?.let { it as ServletRequestAttributes }?.request

    val userIdOrNull: String?
        get() = currentRequest?.getHeader(HEADER_USER_ID)

    val userIdOrThrow: String
        get() = userIdOrNull ?: throw SESSION_NO_FOUND_EXCEPTION

    val userIdOrSystem: String
        get() = userIdOrNull?.toString() ?: authProperties.system.principal

    val timeZone: TimeZone
        get() = getTimeZone()

    val zoneId: ZoneId
        get() = timeZone.toZoneId()

    val locale: Locale
        get() = getLocale()

    val language: String
        get() = locale.language

    val now: OffsetDateTime
        get() = OffsetDateTime.now(zoneId)

    /**
     * Added this here as a utility function to cache objects in to the request scope.
     * For example, you have an external API call that is being called multiple times
     * within the same request lifecycle. Instead of calling the said external API call
     * once and passing the result in to multiple functions, save the result in to this
     * function and reuse the value without passing it into multiple functions.
     */
    inline fun <reified T : Any> requestAttribute(name: String, default: () -> T): T {
        val requestAttributes = getRequestAttributes()!!
        var value = requestAttributes.getAttribute(name, SCOPE_REQUEST) as? T

        if (value !== null) {
            return value
        }

        value = default()

        requestAttributes.setAttribute(name, value, SCOPE_REQUEST)

        return value
    }
}
