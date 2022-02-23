package com.leijendary.spring.boot.template.core.config

import com.leijendary.spring.boot.template.core.util.HEADER_TRACE_ID
import com.leijendary.spring.boot.template.core.util.RequestContext.traceId
import feign.RequestInterceptor
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableFeignClients("com.leijendary.spring.boot.template.client")
class FeignConfiguration {
    @Bean
    fun requestInterceptor(): RequestInterceptor = RequestInterceptor { template ->
        traceId?.let { template.header(HEADER_TRACE_ID, it) }
    }
}