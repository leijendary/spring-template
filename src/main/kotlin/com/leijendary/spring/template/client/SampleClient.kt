package com.leijendary.spring.template.client

import org.springframework.cloud.openfeign.FeignClient
import org.springframework.web.bind.annotation.GetMapping

@FeignClient("google")
interface SampleClient {
    @GetMapping
    fun homepage(): String
}