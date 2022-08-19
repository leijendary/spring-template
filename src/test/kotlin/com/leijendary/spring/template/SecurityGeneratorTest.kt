package com.leijendary.spring.template

import org.junit.jupiter.api.Test
import org.springframework.security.crypto.keygen.KeyGenerators
import java.util.*

class SecurityGeneratorTest : ApplicationTests() {
    private val keyLength = 32

    @Test
    fun generate_key() {
        val bytes = KeyGenerators.secureRandom(keyLength).generateKey()

        println(Base64.getEncoder().encodeToString(bytes))
    }

    @Test
    fun generate_salt() {
        println(KeyGenerators.string().generateKey())
    }
}