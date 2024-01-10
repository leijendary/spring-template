package com.leijendary.filter

import io.micrometer.tracing.Tracer
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

const val HEADER_TRACE_ID = "X-Trace-ID"

@Component
class TraceFilter(private val tracer: Tracer) : OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val span = tracer.currentSpan() ?: tracer.nextSpan()
        val traceId = span.context().traceId()

        response.addHeader(HEADER_TRACE_ID, traceId)

        chain.doFilter(request, response)
    }
}
