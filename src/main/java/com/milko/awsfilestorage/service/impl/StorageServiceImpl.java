package com.milko.awsfilestorage.service.impl;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.milko.awsfilestorage.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.io.File;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    @Value("${application.bucket.name}")
    private String bucketName;

    private final AmazonS3 s3Client;

    public Mono<String> uploadFile(FilePart filePart) {
        log.info("IN StorageService uploadFile(), fileName = {}", filePart.filename());
        String fileName = System.currentTimeMillis() + "_" + filePart.filename();
        File tempFile = new File(fileName);

        return filePart.transferTo(tempFile)
                .then(Mono.fromCallable(() -> {
                    s3Client.putObject(new PutObjectRequest(bucketName, fileName, tempFile));
                    tempFile.delete();
                    return s3Client.getUrl(bucketName, fileName).toString();
                }));
    }
}
