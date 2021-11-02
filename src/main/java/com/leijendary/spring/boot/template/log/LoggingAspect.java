package com.leijendary.spring.boot.template.log;

import com.leijendary.spring.boot.core.config.properties.AspectProperties;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@ConditionalOnExpression("${aspect.logging.enabled:false}")
@Aspect
@Slf4j
public class LoggingAspect {

    private final long time;

    public LoggingAspect(final AspectProperties aspectProperties) {
        this.time = aspectProperties.getExecution().getTime();
    }

    @Around("includedPointcut() && !excludedPointcut()")
    public Object methodTimeLogger(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // Set aspect.execution.time to -1 to disable execution timer
        if (!log.isWarnEnabled() || time < 0) {
            return proceedingJoinPoint.proceed();
        }

        final var methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        // Get intercepted method details
        final var className = methodSignature.getDeclaringType().getSimpleName();
        final var methodName = methodSignature.getName();
        // Measure method execution time
        final var stopWatch = new StopWatch(className + "#" + methodName);
        stopWatch.start(methodName);

        final var result = proceedingJoinPoint.proceed();

        stopWatch.stop();

        final var resultTime = stopWatch.getTotalTimeMillis();

        // Log method execution time if the result time is greater than or equal to the threshold time
        if (resultTime >= time) {
            final var executionTime = String.format("%s running time :: %sms", stopWatch.getId(), resultTime);

            log.warn(executionTime);
        }

        return result;
    }

    @Pointcut("execution(* com.leijendary.spring.boot.template..*.*(..))")
    public void includedPointcut() {}

    @Pointcut(
            "execution(* com.leijendary.spring.boot.template.config..*.*(..)) || " +
            "execution(* com.leijendary.spring.boot.template.filter..*.*(..)) || " +
            "execution(* com.leijendary.spring.boot.template.log..*.*(..)) || " +
            "execution(* com.leijendary.spring.boot.template.Application..*(..))"
    )
    public void excludedPointcut() {}
}
