package com.leijendary.spring.boot.template.service;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.DeleteObjectsRequest;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Profile("aws")
@Service
@RequiredArgsConstructor
public class BucketService extends AppService {

    private final AmazonS3Client amazonS3Client;

    public S3Object get(final String bucketName, final String key) {
        return amazonS3Client.getObject(bucketName, key);
    }

    public S3ObjectInputStream stream(final String bucketName, final String key) {
        final var object = get(bucketName, key);

        return object.getObjectContent();
    }

    public PutObjectResult put(final String bucketName, final String key, final File file) {
        return amazonS3Client.putObject(bucketName, key, file);
    }

    public void render(final String bucketName, final String key, final HttpServletResponse response)
            throws IOException {
        final var object = get(bucketName, key);
        final var metadata = object.getObjectMetadata();
        final var contentType = metadata.getContentType();
        final var outputStream = response.getOutputStream();

        response.setContentType(contentType);

        object.getObjectContent().transferTo(outputStream);
    }

    public void delete(final String bucketName, final String key) {
        amazonS3Client.deleteObject(bucketName, key);
    }

    public void deleteAll(final String bucketName, final List<String> keys) {
        final var request = new DeleteObjectsRequest(bucketName)
                .withKeys(keys.toArray(new String[0]));

        amazonS3Client.deleteObjects(request);
    }
}
