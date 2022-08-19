package com.leijendary.spring.template.core.util

import com.leijendary.spring.template.core.config.properties.EmissionProperties
import com.leijendary.spring.template.core.util.SpringContext.Companion.getBean
import reactor.core.publisher.Sinks.EmitFailureHandler
import reactor.core.publisher.Sinks.EmitFailureHandler.busyLooping

private val emissionProperties = getBean(EmissionProperties::class)

object EmitHandler {
    fun failureHandler(): EmitFailureHandler = busyLooping(emissionProperties.deadline)
}