package com.leijendary.filter

import com.leijendary.context.RequestContext.userIdOrNull
import io.micrometer.tracing.Tracer
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

private const val HEADER = "traceparent"
private const val VERSION = "00"
private const val SAMPLED = "01"
private const val NOT_SAMPLED = "00"
private const val TAG_USER_ID = "user.id"

@Component
class TraceFilter(private val tracer: Tracer) : OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val span = tracer.currentSpan() ?: tracer.nextSpan()
        val context = span.context()
        val flag = if (context.sampled()) SAMPLED else NOT_SAMPLED

        response.addHeader(HEADER, "$VERSION-${context.traceId()}-${context.spanId()}-$flag")

        val userId = userIdOrNull(request)

        if (!userId.isNullOrBlank()) {
            span.tag(TAG_USER_ID, userId)
        }

        chain.doFilter(request, response)
    }
}
