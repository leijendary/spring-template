package com.leijendary.spring.template.core.config

import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.context.annotation.Configuration

@Configuration
@EnableFeignClients("com.leijendary.spring.template.client")
class FeignConfiguration