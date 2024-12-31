package com.leijendary.security

import org.springframework.cloud.bootstrap.encrypt.KeyProperties
import org.springframework.security.crypto.encrypt.Encryptors.delux
import org.springframework.stereotype.Component

@Component
class Encryption(keyProperties: KeyProperties) {
    private val encryptor = delux(keyProperties.key, keyProperties.salt)

    fun encrypt(raw: String): String {
        return encryptor.encrypt(raw)
    }

    fun decrypt(encrypted: String): String {
        return encryptor.decrypt(encrypted)
    }
}
