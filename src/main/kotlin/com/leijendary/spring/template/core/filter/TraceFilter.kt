package com.leijendary.spring.template.core.filter

import io.micrometer.tracing.Tracer
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

private const val TRACE_ID_HEADER = "X-Trace-ID"

@Component
@Order(Ordered.LOWEST_PRECEDENCE)
class TraceFilter(private val tracer: Tracer) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain
    ) {
        val traceId = tracer.currentSpan()?.context()?.traceId()
        traceId?.let {
            response.addHeader(TRACE_ID_HEADER, traceId)
        }

        filterChain.doFilter(request, response)
    }
}
