package com.leijendary.filter

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

@Component
class TraceFilter(private val tracer: Tracer) : OncePerRequestFilter() {
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val span = tracer.currentSpan()?.context() ?: tracer.nextSpan().context()
        val flag = if (span.sampled()) SAMPLED else NOT_SAMPLED

        response.addHeader(HEADER, "$VERSION-${span.traceId()}-${span.spanId()}-$flag")

        chain.doFilter(request, response)
    }
}
