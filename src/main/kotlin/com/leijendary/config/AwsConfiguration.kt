package com.leijendary.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider
import software.amazon.awssdk.regions.providers.AwsRegionProvider
import software.amazon.awssdk.services.cloudfront.CloudFrontClient

@Configuration(proxyBeanMethods = false)
class AwsConfiguration(
    private val awsCredentialsProvider: AwsCredentialsProvider,
    private val awsRegionProvider: AwsRegionProvider
) {
    @Bean
    fun cloudFrontClient(): CloudFrontClient {
        return CloudFrontClient.builder()
            .credentialsProvider(awsCredentialsProvider)
            .region(awsRegionProvider.region)
            .build()
    }
}
