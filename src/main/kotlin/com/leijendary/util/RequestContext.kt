package com.leijendary.util

import com.leijendary.error.exception.StatusException
import com.leijendary.model.ErrorSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpStatus.UNAUTHORIZED
import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.time.ZoneId
import java.util.*

private const val HEADER_USER_ID = "X-User-ID"
private const val USER_SYSTEM = "System"
private val SESSION_NOT_FOUND_EXCEPTION = StatusException(
    code = "access.session.notFound",
    status = UNAUTHORIZED,
    source = ErrorSource(header = HEADER_USER_ID)
)

@Component
@RequestScope
class RequestContext {
    val attributes by lazy { RequestContextHolder.currentRequestAttributes() }
    val currentRequest by lazy { (attributes as ServletRequestAttributes).request }
    val userIdOrNull: String? by lazy { currentRequest.getHeader(HEADER_USER_ID) }
    val userIdOrSystem: String by lazy { userIdOrNull ?: USER_SYSTEM }
    val userIdOrThrow: String by lazy { userIdOrNull ?: throw SESSION_NOT_FOUND_EXCEPTION }
    val timeZone: TimeZone by lazy { LocaleContextHolder.getTimeZone() }
    val zoneId: ZoneId by lazy { timeZone.toZoneId() }
    val locale: Locale by lazy { LocaleContextHolder.getLocale() }
    val language: String by lazy { locale.language }

    /**
     * Added this here as a utility function to cache objects in to the request scope.
     * For example, you have an external API call that is being called multiple times
     * within the same request lifecycle. Instead of calling the said external API call
     * once and passing the result in to multiple functions, save the result in to this
     * function and reuse the value without passing it into multiple functions.
     */
    fun <T : Any> attribute(name: String, default: () -> T?): T? {
        @Suppress("UNCHECKED_CAST")
        var value = currentRequest.getAttribute(name) as? T

        if (value !== null) {
            return value
        }

        value = default()

        currentRequest.setAttribute(name, value)

        return value
    }
}
