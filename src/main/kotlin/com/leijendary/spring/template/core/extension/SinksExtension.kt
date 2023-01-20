package com.leijendary.spring.template.core.extension

import com.leijendary.spring.template.core.util.EmitHandler.failureHandler
import reactor.core.publisher.Sinks

fun <T> Sinks.Many<T>.emit(t: T) = this.emitNext(t!!, failureHandler())
