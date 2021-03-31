package com.leijendary.spring.microservicetemplate.log;

import com.leijendary.spring.microservicetemplate.config.properties.AspectProperties;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@ConditionalOnExpression("${aspect.logging.enabled}")
@Aspect
@RequiredArgsConstructor
public class LoggingAspect {

    public static final Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

    private final AspectProperties aspectProperties;

    @Around("execution(* com.leijendary.spring.microservicetemplate.client..*.*(..)) || " +
            "execution(* com.leijendary.spring.microservicetemplate.controller..*.*(..)) || " +
            "execution(* com.leijendary.spring.microservicetemplate.data..*.*(..)) || " +
            "execution(* com.leijendary.spring.microservicetemplate.error..*.*(..)) || " +
            "execution(* com.leijendary.spring.microservicetemplate.event..*.*(..)) || " +
            "execution(* com.leijendary.spring.microservicetemplate.exception..*.*(..)) || " +
            "execution(* com.leijendary.spring.microservicetemplate.factory..*.*(..)) || " +
            "execution(* com.leijendary.spring.microservicetemplate.model..*.*(..)) || " +
            "execution(* com.leijendary.spring.microservicetemplate.repository..*.*(..)) || " +
            "execution(* com.leijendary.spring.microservicetemplate.security..*.*(..)) || " +
            "execution(* com.leijendary.spring.microservicetemplate.service..*.*(..)) || " +
            "execution(* com.leijendary.spring.microservicetemplate.specification..*.*(..)) || " +
            "execution(* com.leijendary.spring.microservicetemplate.util..*.*(..)) || " +
            "execution(* com.leijendary.spring.microservicetemplate.validator..*.*(..)) ")
    public Object methodTimeLogger(final ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        final var methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        // Get intercepted method details
        final var className = methodSignature.getDeclaringType().getSimpleName();
        final var methodName = methodSignature.getName();
        // Measure method execution time
        final var stopWatch = new StopWatch(className + "#" + methodName);
        stopWatch.start(methodName);

        final var result = proceedingJoinPoint.proceed();

        stopWatch.stop();

        final var timeResult = stopWatch.getTotalTimeMillis();
        final var time = aspectProperties.getExecution().getTime();

        // Log method execution time
        if (logger.isWarnEnabled() && timeResult >= time) {
            final var executionTime = String.format("'%s' running time :: %sms", stopWatch.getId(), timeResult);

            logger.warn(executionTime);
        }

        return result;
    }
}
