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

import java.util.function.Supplier;

@Component
@ConditionalOnExpression("${aspect.logging.enabled}")
@Aspect
public class LoggingAspect {

    public static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    private final long time;

    public LoggingAspect(final AspectProperties aspectProperties) {
        this.time = aspectProperties.getExecution().getTime();
    }

    @Around("execution(* com.leijendary.spring.microservicetemplate..*.*(..)) && !excludedPackages()")
    public Object methodTimeLogger(final ProceedingJoinPoint proceedingJoinPoint) {
        final var proceed = (Supplier<Object>) () -> {
            try {
                return proceedingJoinPoint.proceed();
            } catch (Throwable throwable) {
                throw new RuntimeException(throwable);
            }
        };

        // Set aspect.execution.time to -1 to disable execution timer
        if (time < 0) {
            return proceed.get();
        }

        final var methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        // Get intercepted method details
        final var className = methodSignature.getDeclaringType().getSimpleName();
        final var methodName = methodSignature.getName();
        // Measure method execution time
        final var stopWatch = new StopWatch(className + "#" + methodName);
        stopWatch.start(methodName);

        final var result = proceed.get();

        stopWatch.stop();

        final var resultTime = stopWatch.getTotalTimeMillis();

        // Log method execution time
        if (logger.isWarnEnabled() && resultTime >= time) {
            final var executionTime = String.format("%s running time :: %sms", stopWatch.getId(), resultTime);

            logger.warn(executionTime);
        }

        return result;
    }

    @Pointcut("execution(* com.leijendary.spring.microservicetemplate.config..*.*(..)) || " +
              "execution(* com.leijendary.spring.microservicetemplate.filter..*.*(..)) || " +
              "execution(* com.leijendary.spring.microservicetemplate.log..*.*(..)) || " +
              "execution(* com.leijendary.spring.microservicetemplate.MicroserviceTemplateApplication..*(..))")
    public void excludedPackages() {}
}
