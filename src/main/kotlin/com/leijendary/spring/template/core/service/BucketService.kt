package com.leijendary.spring.template.core.service

import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.services.s3.model.*
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import java.io.File
import java.io.IOException
import javax.servlet.ServletOutputStream
import javax.servlet.http.HttpServletResponse

@Profile("aws")
@Service
class BucketService(private val amazonS3Client: AmazonS3Client) {
    operator fun get(bucketName: String?, key: String?): S3Object {
        return amazonS3Client.getObject(bucketName, key)
    }

    fun stream(bucketName: String?, key: String?): S3ObjectInputStream {
        val s3Object: S3Object = get(bucketName, key)

        return s3Object.objectContent
    }

    fun put(bucketName: String?, key: String?, file: File?): PutObjectResult {
        return amazonS3Client.putObject(bucketName, key, file)
    }

    @Throws(IOException::class)
    fun render(bucketName: String?, key: String?, response: HttpServletResponse) {
        val s3Object: S3Object = get(bucketName, key)
        val metadata: ObjectMetadata = s3Object.objectMetadata
        val contentType: String = metadata.contentType
        val outputStream: ServletOutputStream = response.outputStream

        response.contentType = contentType

        s3Object.objectContent.transferTo(outputStream)
    }

    fun delete(bucketName: String?, key: String?) {
        amazonS3Client.deleteObject(bucketName, key)
    }

    fun deleteAll(bucketName: String?, keys: List<String>) {
        val request: DeleteObjectsRequest = DeleteObjectsRequest(bucketName).withKeys(*keys.toTypedArray())

        amazonS3Client.deleteObjects(request)
    }
}