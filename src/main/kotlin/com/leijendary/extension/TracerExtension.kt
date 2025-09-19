package com.leijendary.extension

import io.micrometer.tracing.Span
import io.micrometer.tracing.Tracer
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletableFuture.supplyAsync

private const val TAG_ASYNC = "async"

fun Tracer.currentOrNextSpan(): Span {
    val span = currentSpan()

    return span ?: nextSpan()
}

fun <U> Tracer.supplyAsyncSpan(supplier: () -> U): CompletableFuture<U> {
    val span = nextSpan()
    span.tag(TAG_ASYNC, true)

    return supplyAsync {
        withSpan(span).use { supplier() }
    }
}
