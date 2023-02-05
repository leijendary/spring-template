package com.leijendary.spring.template.core.storage

import com.leijendary.spring.template.core.config.properties.AwsS3Properties
import jakarta.servlet.http.HttpServletResponse
import org.springframework.stereotype.Service
import software.amazon.awssdk.core.ResponseInputStream
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.*
import java.io.File

@Service
class S3Storage(awsS3Properties: AwsS3Properties, private val s3Client: S3Client) {
    private val bucketName = awsS3Properties.bucketName

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
