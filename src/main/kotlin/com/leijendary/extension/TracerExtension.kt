package com.leijendary.extension

import io.micrometer.tracing.Span
import io.micrometer.tracing.Tracer
import java.util.concurrent.CompletableFuture
import java.util.concurrent.CompletableFuture.supplyAsync

fun Tracer.currentOrNextSpan(): Span {
    val span = currentSpan()

    return span ?: nextSpan()
}

fun <U> Tracer.supplyAsyncSpan(supplier: () -> U): CompletableFuture<U> {
    val span = nextSpan()

    return supplyAsync {
        withSpan(span).use { supplier() }
    }
}
