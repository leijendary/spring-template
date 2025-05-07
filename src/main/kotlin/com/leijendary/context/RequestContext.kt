package com.leijendary.context

import com.leijendary.error.CODE_SESSION_NOT_FOUND
import com.leijendary.error.exception.StatusException
import com.leijendary.model.ErrorSource
import jakarta.servlet.http.HttpServletRequest
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.time.ZoneId
import java.util.*

private const val HEADER_USER_ID = "X-User-ID"
private const val USER_SYSTEM = "System"

private val SESSION_NOT_FOUND_EXCEPTION = StatusException(
    code = CODE_SESSION_NOT_FOUND,
    status = UNAUTHORIZED,
    source = ErrorSource(header = HEADER_USER_ID)
)

object RequestContext {
    val currentRequest
        get() = (RequestContextHolder.getRequestAttributes() as? ServletRequestAttributes)?.request

    val userIdOrNull: String?
        get() = currentRequest?.let(::userIdOrNull)

    val userIdOrSystem: String
        get() = userIdOrNull ?: USER_SYSTEM

    val userIdOrThrow: String
        get() = userIdOrNull ?: throw SESSION_NOT_FOUND_EXCEPTION

    val timeZone: TimeZone
        get() = LocaleContextHolder.getTimeZone()

    val zoneId: ZoneId
        get() = timeZone.toZoneId()

    val locale: Locale
        get() = LocaleContextHolder.getLocale()

    val language: String
        get() = locale.language

    val isApi: Boolean
        get() = currentRequest?.servletPath?.startsWith("/api") == true

    val isFragment: Boolean
        get() = currentRequest?.getHeader("hx-request") == "true"

    fun userIdOrNull(request: HttpServletRequest): String? {
        return request.getHeader(HEADER_USER_ID)
    }

    /**
     * Added this here as a utility function to cache objects in to the request scope.
     * For example, you have an external API call that is being called multiple times
     * within the same request lifecycle. Instead of calling the said external API call
     * once and passing the result in to multiple functions, save the result in to this
     * function and reuse the value without passing it into multiple functions.
     */
    fun <T : Any> attribute(name: String, default: () -> T): T {
        val request = currentRequest ?: throw IllegalStateException("No thread-bound request found")

        @Suppress("UNCHECKED_CAST")
        var value = request.getAttribute(name) as? T

        if (value !== null) {
            return value
        }

        value = default()
        request.setAttribute(name, value)

        return value
    }
}
