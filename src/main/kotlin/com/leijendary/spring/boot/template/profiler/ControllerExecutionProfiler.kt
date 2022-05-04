package com.leijendary.spring.boot.template.profiler

import com.leijendary.spring.boot.template.core.extension.logger
import org.aspectj.lang.ProceedingJoinPoint
import org.aspectj.lang.annotation.After
import org.aspectj.lang.annotation.Around
import org.aspectj.lang.annotation.Aspect
import org.springframework.stereotype.Component
import org.springframework.util.unit.DataSize

@Component
@Aspect
class ControllerExecutionProfiler {
    private val log = logger()

    @Around("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    fun profile(pjp: ProceedingJoinPoint): Any {
        val start = System.currentTimeMillis()

        log.info("ControllerExecutionProfiler.profile(): Going to call the method: {}", pjp.signature.name)

        val output = pjp.proceed()

        log.info("ControllerExecutionProfiler.profile(): Method execution completed.")

        val elapsedTime = System.currentTimeMillis() - start

        log.info("ControllerExecutionProfiler.profile(): Method execution time: $elapsedTime milliseconds.")

        return output
    }

    @After("@annotation(org.springframework.web.bind.annotation.PostMapping)")
    fun profileMemory() {
        val memoryUsed = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()
        log.info(
            "JVM memory in use = {}",
            DataSize.ofBytes(memoryUsed).toKilobytes()
        )
    }
}