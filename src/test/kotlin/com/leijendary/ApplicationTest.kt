package com.leijendary

import org.springframework.boot.fromApplication
import org.springframework.boot.with

fun main() {
    fromApplication<Application>()
        .with(TestcontainersConfiguration::class)
        .run()
}
