package com.leijendary.spring.microservicetemplate.log;

import com.leijendary.spring.microservicetemplate.config.properties.AspectProperties;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@ConditionalOnExpression("${aspect.logging.enabled:false}")
@Aspect
public class LoggingAspect {

    public static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    private final long time;

    public LoggingAspect(final AspectProperties aspectProperties) {
        this.time = aspectProperties.getExecution().getTime();
    }

    @Around("includedPointcut() && !excludedPointcut()")
    public Object methodTimeLogger(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        // Set aspect.execution.time to -1 to disable execution timer
        if (!logger.isWarnEnabled() || time < 0) {
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

            logger.warn(executionTime);
        }

        return result;
    }

    @Pointcut("execution(* com.leijendary.spring.microservicetemplate..*.*(..))")
    public void includedPointcut() {}

    @Pointcut(
            "execution(* com.leijendary.spring.microservicetemplate.config..*.*(..)) || " +
            "execution(* com.leijendary.spring.microservicetemplate.filter..*.*(..)) || " +
            "execution(* com.leijendary.spring.microservicetemplate.log..*.*(..)) || " +
            "execution(* com.leijendary.spring.microservicetemplate.Application..*(..))")
    public void excludedPointcut() {}
}
