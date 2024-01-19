package com.milko.awsfilestorage.service;

import com.milko.awsfilestorage.dto.FileDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FileService {
    Mono<FileDto> createFile(FileDto fileDto, Integer userId);

    Mono<FileDto> updateFile(FileDto fileDto);

    Mono<FileDto> getFileById(Integer id);

    Flux<FileDto> getAllFilesByUserId(Integer id);

    Flux<FileDto> getAllFiles();

    Mono<Void> deleteFileById(Integer id);
}
