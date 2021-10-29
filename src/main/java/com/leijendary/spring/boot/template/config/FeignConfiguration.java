package com.leijendary.spring.boot.template.config;

import com.leijendary.spring.boot.template.client.AppClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackageClasses = AppClient.class)
public class FeignConfiguration {
}
