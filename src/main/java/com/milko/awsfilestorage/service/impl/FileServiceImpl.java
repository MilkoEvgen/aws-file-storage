package com.milko.awsfilestorage.service.impl;

import com.milko.awsfilestorage.dto.FileDto;
import com.milko.awsfilestorage.mapper.FileMapper;
import com.milko.awsfilestorage.model.EventEntity;
import com.milko.awsfilestorage.model.FileEntity;
import com.milko.awsfilestorage.model.Status;
import com.milko.awsfilestorage.repository.EventRepository;
import com.milko.awsfilestorage.repository.FileRepository;
import com.milko.awsfilestorage.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;
    private final EventRepository eventRepository;
    private final FileMapper fileMapper;

    public Mono<FileDto> createFile(FileDto fileDto, Integer userId) {
        log.info("IN FileService createFile(), fileDto: {}, userId: {}", fileDto, userId);
        Mono<FileEntity> fileMono = fileRepository.save(fileMapper.map(fileDto));
        return fileMono
                .flatMap(file -> {
                    EventEntity eventEntity = EventEntity.builder()
                            .userId(userId)
                            .fileId(file.getId())
                            .status(Status.ACTIVE)
                            .build();
                    return eventRepository.save(eventEntity)
                            .thenReturn(file);
                }).map(fileMapper::map);
    }

    public Mono<FileDto> updateFile(FileDto fileDto) {
        log.info("IN FileService updateFile(), fileDto: {}", fileDto);
        FileEntity fileEntity = fileMapper.map(fileDto);
        return fileRepository.save(fileEntity)
                .map(fileMapper::map);
    }

    public Mono<FileDto> getFileById(Integer id) {
        log.info("IN FileService getFileById(), id: {}", id);
        return fileRepository.findById(id)
                .map(fileMapper::map);
    }

    public Flux<FileDto> getAllFilesByUserId(Integer id) {
        log.info("IN FileService getAllFilesByUserId(), userId: {}", id);
        return fileRepository.getAllFilesByUserId(id)
                .map(fileMapper::map);
    }

    public Flux<FileDto> getAllFiles() {
        log.info("IN FileService getAllFiles()");
        return fileRepository.findAll()
                .map(fileMapper::map);
    }

    public Mono<Void> deleteFileById(Integer id) {
        log.info("IN FileService deleteFileById(), id: {}", id);
        return fileRepository.deleteFile(id);
    }
}
