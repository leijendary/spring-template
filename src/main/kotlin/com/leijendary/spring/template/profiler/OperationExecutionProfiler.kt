package com.leijendary.spring.template.profiler

import com.leijendary.spring.template.core.extension.logger
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import org.springframework.util.unit.DataSize
import java.lang.Runtime.getRuntime
import kotlin.system.measureTimeMillis

@Component
@Aspect
@Profile("debug")
class OperationExecutionProfiler {
    private val log = logger()

    @Around("@annotation(io.swagger.v3.oas.annotations.Operation)")
    fun profile(proceedingJoinPoint: ProceedingJoinPoint): Any {
        var output: Any

        val time = measureTimeMillis {
            output = proceedingJoinPoint.proceed()
        }

        val operation = proceedingJoinPoint.signature.declaringType.simpleName
        val method = proceedingJoinPoint.signature.name
        val memoryUse = DataSize
            .ofBytes(getRuntime().totalMemory() - getRuntime().freeMemory())
            .toKilobytes()

        log.info("Method $operation#$method completed in $time milliseconds. JVM memory in use: $memoryUse")

        return output
    }
}