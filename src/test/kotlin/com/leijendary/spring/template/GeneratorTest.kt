package com.leijendary.spring.template

import org.junit.jupiter.api.Test
import org.springframework.security.crypto.keygen.KeyGenerators
import java.util.*

/**
 * This is outside of code testing, but more on generating what is needed for the application like
 * encryption keys.
 */
class GeneratorTest : ApplicationTests() {
    private val keyLength = 32

    @Test
    fun `Generate Key`() {
        val bytes = KeyGenerators.secureRandom(keyLength).generateKey()

        println(Base64.getEncoder().encodeToString(bytes))
    }

    @Test
    fun `Generate Salt`() {
        println(KeyGenerators.string().generateKey())
    }
}
