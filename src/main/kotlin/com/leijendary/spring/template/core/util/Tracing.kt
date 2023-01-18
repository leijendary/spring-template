package com.leijendary.spring.template.core.util

import org.slf4j.MDC

private const val MDC_TRACE_ID = "traceId"
private const val MDC_SPAN_ID = "spanId"

fun traced(traceParent: String, function: () -> Unit) {
    val trace = traceParent.split("-")

    MDC.put(MDC_TRACE_ID, trace[1])
    MDC.put(MDC_SPAN_ID, trace[2])

    function()

    MDC.remove(MDC_TRACE_ID)
    MDC.remove(MDC_SPAN_ID)
}
