package com.leijendary.filter

import com.leijendary.util.Tracing
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

const val HEADER_TRACE_ID = "X-Trace-ID"

@Component
class TraceFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val traceId = Tracing.get().traceId()

        response.addHeader(HEADER_TRACE_ID, traceId)

        filterChain.doFilter(request, response)
    }
}
