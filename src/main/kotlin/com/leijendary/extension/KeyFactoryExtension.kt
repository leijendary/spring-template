package com.leijendary.extension

import java.security.KeyFactory
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.X509EncodedKeySpec
import java.util.*

fun KeyFactory.rsaPrivateKey(privateKey: String): RSAPrivateKey {
    val decoded = Base64.getDecoder().decode(privateKey)
    val keySpec = PKCS8EncodedKeySpec(decoded)

    return generatePrivate(keySpec) as RSAPrivateKey
}

fun KeyFactory.rsaPublicKey(publicKey: String): RSAPublicKey {
    val decoded = Base64.getDecoder().decode(publicKey)
    val keySpec = X509EncodedKeySpec(decoded)

    return generatePublic(keySpec) as RSAPublicKey
}
