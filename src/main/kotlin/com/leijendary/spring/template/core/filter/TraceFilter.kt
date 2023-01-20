package com.leijendary.spring.template.core.filter

import com.leijendary.spring.template.core.util.Tracing
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

const val TRACE_ID_HEADER = "X-Trace-ID"

@Component
class TraceFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val traceId = Tracing.get().traceId()

        response.addHeader(TRACE_ID_HEADER, traceId)

        filterChain.doFilter(request, response)
    }
}
