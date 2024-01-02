package com.leijendary.storage

import com.leijendary.config.properties.AwsCloudFrontProperties
import com.leijendary.config.properties.AwsS3Properties
import com.leijendary.extension.rsaPrivateKey
import com.leijendary.projection.ImageProjection
import com.leijendary.storage.BlockStorage.Request.GET
import com.leijendary.storage.BlockStorage.Request.PUT
import jakarta.servlet.http.HttpServletResponse
import org.bouncycastle.jce.provider.BouncyCastleProvider
import org.springframework.stereotype.Service
import software.amazon.awssdk.core.ResponseInputStream
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.cloudfront.CloudFrontClient
import software.amazon.awssdk.services.cloudfront.CloudFrontUtilities
import software.amazon.awssdk.services.cloudfront.model.CannedSignerRequest
import software.amazon.awssdk.services.cloudfront.model.CreateInvalidationRequest
import software.amazon.awssdk.services.cloudfront.model.InvalidationBatch
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.*
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.io.File
import java.security.KeyFactory
import java.time.Instant
import java.util.concurrent.CompletableFuture.supplyAsync

@Service
class BlockStorage(
    private val awsCloudFrontProperties: AwsCloudFrontProperties,
    private val awsS3Properties: AwsS3Properties,
    private val cloudFrontClient: CloudFrontClient,
    private val s3Client: S3Client,
    private val s3Presigner: S3Presigner
) {
    private val cloudFrontUtilities = CloudFrontUtilities.create()
    private val privateKey = KeyFactory.getInstance("RSA", BouncyCastleProvider())
        .rsaPrivateKey(awsCloudFrontProperties.privateKey)

    enum class Request {
        GET,
        PUT
    }

    fun sign(image: ImageProjection, request: Request = GET): ImageProjection {
        val original = supplyAsync { sign(image.original, request) }
        val preview = supplyAsync { sign(image.preview, request) }
        val thumbnail = supplyAsync { sign(image.thumbnail, request) }

        return image.apply {
            this.original = original.get()
            this.preview = preview.get()
            this.thumbnail = thumbnail.get()
        }
    }

    fun sign(key: String, request: Request = GET) = when (request) {
        GET -> signGet(key)
        PUT -> signPut(key)
    }

    fun signGet(key: String): String {
        val resourceUrl = "${awsCloudFrontProperties.url}/$key"
        val expiry = Instant.now().plus(awsCloudFrontProperties.signatureDuration)
        val signerRequest = CannedSignerRequest.builder()
            .keyPairId(awsCloudFrontProperties.publicKeyId)
            .resourceUrl(resourceUrl)
            .privateKey(privateKey)
            .expirationDate(expiry)
            .build()

        return cloudFrontUtilities.getSignedUrlWithCannedPolicy(signerRequest).url()
    }

    fun signPut(key: String): String {
        val request = PutObjectRequest.builder()
            .bucket(awsS3Properties.bucketName)
            .key(key)
            .build()
        val signRequest = PutObjectPresignRequest.builder()
            .putObjectRequest(request)
            .signatureDuration(awsS3Properties.signatureDuration)
            .build()

        return s3Presigner.presignPutObject(signRequest).url().toString()
    }

    fun copy(sourceKey: String, destinationKey: String): CopyObjectResponse {
        val request = CopyObjectRequest.builder()
            .sourceBucket(awsS3Properties.bucketName)
            .destinationBucket(awsS3Properties.bucketName)
            .sourceKey(sourceKey)
            .destinationKey(destinationKey)
            .build()

        return s3Client.copyObject(request)
    }

    fun get(key: String): GetObjectResponse {
        val s3Object = stream(key)

        return s3Object.response()
    }

    fun head(key: String): HeadObjectResponse {
        val request = HeadObjectRequest.builder()
            .bucket(awsS3Properties.bucketName)
            .key(key)
            .build()

        return s3Client.headObject(request)
    }

    fun exists(key: String): Boolean = try {
        head(key)
        true
    } catch (_: NoSuchKeyException) {
        false
    }

    fun invalidateCache(reference: String, vararg keys: String) {
        val items = keys.map { if (it.startsWith("/")) it else "/$it" }
        val callerReference = "$reference:${Instant.now().epochSecond}"
        val batch = InvalidationBatch.builder()
            .paths { it.items(items).quantity(keys.size) }
            .callerReference(callerReference)
            .build()
        val request = CreateInvalidationRequest.builder()
            .distributionId(awsCloudFrontProperties.distributionId)
            .invalidationBatch(batch)
            .build()

        cloudFrontClient.createInvalidation(request)
    }

    fun stream(key: String): ResponseInputStream<GetObjectResponse> {
        val request = GetObjectRequest.builder()
            .bucket(awsS3Properties.bucketName)
            .key(key)
            .build()

        return s3Client.getObject(request)
    }

    fun put(key: String, file: File): PutObjectResponse {
        val request = PutObjectRequest.builder()
            .bucket(awsS3Properties.bucketName)
            .key(key)
            .build()
        val body = RequestBody.fromFile(file)

        return s3Client.putObject(request, body)
    }

    fun render(key: String, servletResponse: HttpServletResponse) {
        val objectStream = stream(key)
        val s3Object = objectStream.response()
        val contentType = s3Object.contentType()
        val outputStream = servletResponse.outputStream

        servletResponse.contentType = contentType

        objectStream.transferTo(outputStream)
    }

    fun delete(key: String): DeleteObjectResponse {
        val request = DeleteObjectRequest.builder()
            .bucket(awsS3Properties.bucketName)
            .key(key)
            .build()

        return s3Client.deleteObject(request)
    }

    fun deleteAll(keys: List<String>): DeleteObjectsResponse {
        val ids = keys.map { key -> ObjectIdentifier.builder().key(key).build() }
        val request = DeleteObjectsRequest.builder()
            .bucket(awsS3Properties.bucketName)
            .delete { it.objects(ids).build() }
            .build()

        return s3Client.deleteObjects(request)
    }
}
