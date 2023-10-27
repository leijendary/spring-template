package com.leijendary.spring.core.security

import org.springframework.cloud.bootstrap.encrypt.KeyProperties
import org.springframework.security.crypto.encrypt.Encryptors.delux
import org.springframework.security.crypto.encrypt.TextEncryptor
import org.springframework.stereotype.Component

@Component
class Encryption(private val keyProperties: KeyProperties) {
    fun encrypt(raw: String?): String {
        return encryptor().encrypt(raw)
    }

    fun decrypt(encrypted: String?): String {
        return encryptor().decrypt(encrypted)
    }

    private fun encryptor(): TextEncryptor {
        return delux(keyProperties.key, keyProperties.salt)
    }
}
