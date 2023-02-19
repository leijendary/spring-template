package com.leijendary.spring.template.core.storage

import com.leijendary.spring.template.core.config.properties.AwsS3Properties
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Service
import software.amazon.awssdk.core.ResponseInputStream
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.*
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.io.File

@Service
class S3Storage(
    awsS3Properties: AwsS3Properties,
    private val s3Client: S3Client,
    private val s3Presigner: S3Presigner
) {
    private val bucketName = awsS3Properties.bucketName

    enum class Request {
        GET,
        PUT
    }

    fun sign(key: String, request: Request = Request.GET) = when (request) {
        Request.GET -> signGet(key)
        Request.PUT -> signPut(key)
    }

    fun signGet(key: String): String {
        val request = GetObjectRequest
            .builder()
            .bucket(bucketName)
            .key(key)
            .build()
        val signRequest = GetObjectPresignRequest
            .builder()
            .getObjectRequest(request)
            .build()

        return s3Presigner
            .presignGetObject(signRequest)
            .url()
            .toString()
    }

    fun signPut(key: String): String {
        val request = PutObjectRequest
            .builder()
            .bucket(bucketName)
            .key(key)
            .build()
        val signRequest = PutObjectPresignRequest
            .builder()
            .putObjectRequest(request)
            .build()

        return s3Presigner
            .presignPutObject(signRequest)
            .url()
            .toString()
    }

    fun get(key: String): GetObjectResponse {
        val s3Object = stream(key)

        return s3Object.response()
    }

    fun stream(key: String): ResponseInputStream<GetObjectResponse> {
        val request = GetObjectRequest.builder()
            .bucket(bucketName)
            .key(key)
            .build()

        return s3Client.getObject(request)
    }

    fun put(key: String, file: File): PutObjectResponse {
        val request = PutObjectRequest.builder()
            .bucket(bucketName)
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
            .bucket(bucketName)
            .key(key)
            .build()

        return s3Client.deleteObject(request)
    }

    fun deleteAll(keys: List<String>): DeleteObjectsResponse {
        val ids = keys.map { key -> ObjectIdentifier.builder().key(key).build() }
        val request = DeleteObjectsRequest.builder()
            .bucket(bucketName)
            .delete {
                it.objects(ids).build()
            }
            .build()

        return s3Client.deleteObjects(request)
    }
}
