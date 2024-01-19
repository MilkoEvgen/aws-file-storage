package com.milko.awsfilestorage.service;

import org.springframework.http.codec.multipart.FilePart;
import reactor.core.publisher.Mono;

public interface StorageService {
    Mono<String> uploadFile(FilePart filePart);
}
