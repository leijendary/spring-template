package com.leijendary.spring.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/samples")
class SampleRest {
    @GetMapping
    fun get() = Thread.currentThread().threadGroup.name
}
