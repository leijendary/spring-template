package com.leijendary.config.properties

import com.leijendary.extension.rsaPrivateKey
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.boot.context.properties.ConfigurationProperties
import java.security.KeyFactory
import java.time.Duration

@ConfigurationProperties("aws.cloud-front", ignoreUnknownFields = false)
data class AwsCloudFrontProperties(
    val distributionId: String,
    val url: String,
    val publicKeyId: String,
    val privateKey: String,
    val signatureDuration: Duration = Duration.ofMinutes(30)
) {
    val rsaPrivateKey = KeyFactory.getInstance("RSA", BouncyCastleProvider()).rsaPrivateKey(privateKey)
}
