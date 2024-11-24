package com.leijendary

import org.springframework.boot.fromApplication
import org.springframework.boot.with

fun main(args: Array<String>) {
    fromApplication<Application>()
        .with(TestcontainersConfiguration::class)
        .run(*args)
}
