package com.milko.awsfilestorage;

import com.milko.awsfilestorage.dto.FileDto;
import com.milko.awsfilestorage.mapper.FileMapper;
import com.milko.awsfilestorage.model.EventEntity;
import com.milko.awsfilestorage.model.FileEntity;
import com.milko.awsfilestorage.model.Status;
import com.milko.awsfilestorage.repository.EventRepository;
import com.milko.awsfilestorage.repository.FileRepository;
import com.milko.awsfilestorage.service.impl.FileServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class FileServiceImplTest {

    @Mock
    private FileRepository fileRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private FileMapper fileMapper;
    @InjectMocks
    private FileServiceImpl fileServiceImpl;
    private FileEntity file;
    private FileDto fileDto;
    private EventEntity event;

    @BeforeEach
    public void init(){
        file = FileEntity.builder()
                .id(1)
                .location("location")
                .status(Status.ACTIVE)
                .build();
        fileDto = FileDto.builder()
                .id(1)
                .location("location")
                .status(Status.ACTIVE)
                .build();
        event = EventEntity.builder()
                .id(1)
                .userId(1)
                .fileId(1)
                .status(Status.ACTIVE)
                .build();
    }

    @Test
    public void createFileShouldReturnFileDto(){
        Mockito.when(fileMapper.map(any(FileDto.class))).thenReturn(file);
        Mockito.when(fileRepository.save(any(FileEntity.class))).thenReturn(Mono.just(file));
        Mockito.when(eventRepository.save(any(EventEntity.class))).thenReturn(Mono.just(event));
        Mockito.when(fileMapper.map(any(FileEntity.class))).thenReturn(fileDto);

        Mono<FileDto> result = fileServiceImpl.createFile(fileDto, 1);

        StepVerifier.create(result)
                .expectNextMatches(dto -> {
                    return dto.getId() == 1 &&
                            dto.getLocation().equals("location") &&
                            dto.getStatus().equals(Status.ACTIVE);
                }).expectComplete()
                .verify();
        Mockito.verify(fileMapper).map(any(FileEntity.class));
        Mockito.verify(fileMapper).map(any(FileDto.class));
        Mockito.verify(fileRepository).save(any(FileEntity.class));
        Mockito.verify(eventRepository).save(any(EventEntity.class));
    }


    @Test
    public void getFileByIdShouldReturnFileDto(){
        Mockito.when(fileRepository.findById(1)).thenReturn(Mono.just(file));
        Mockito.when(fileMapper.map(file)).thenReturn(fileDto);

        Mono<FileDto> result = fileServiceImpl.getFileById(1);

        StepVerifier.create(result)
                .expectNextMatches(dto -> {
                    return dto.getId() == 1 &&
                            dto.getLocation().equals("location") &&
                            dto.getStatus() == Status.ACTIVE;
                })
                .expectComplete()
                .verify();
        Mockito.verify(fileRepository).findById(1);
    }

    @Test
    public void getFileByIdShouldReturnEmptyMono(){
        Mockito.when(fileRepository.findById(1)).thenReturn(Mono.empty());
        Mono<FileDto> result = fileServiceImpl.getFileById(1);
        StepVerifier.create(result)
                .expectComplete()
                .verify();
        Mockito.verify(fileRepository).findById(1);
    }

    @Test
    public void getAllFilesByUserIdShouldReturnListOfFileDto() {
        FileEntity file2 = FileEntity.builder()
                .id(2)
                .location("location2")
                .status(Status.DELETED)
                .build();
        FileDto fileDto2 = FileDto.builder()
                .id(2)
                .location("location2")
                .status(Status.DELETED)
                .build();

        Mockito.when(fileRepository.getAllFilesByUserId(1)).thenReturn(Flux.just(file, file2));
        Mockito.when(fileMapper.map(file)).thenReturn(fileDto);
        Mockito.when(fileMapper.map(file2)).thenReturn(fileDto2);

        Flux<FileDto> result = fileServiceImpl.getAllFilesByUserId(1);

        StepVerifier.create(result)
                .expectNextMatches(dto -> {
                    return dto.getId() == 1 &&
                            dto.getLocation().equals("location") &&
                            dto.getStatus() == Status.ACTIVE;
                })
                .expectNextMatches(dto -> {
                    return dto.getId() == 2 &&
                            dto.getLocation().equals("location2") &&
                            dto.getStatus() == Status.DELETED;
                })
                .expectComplete()
                .verify();
        Mockito.verify(fileRepository).getAllFilesByUserId(1);
    }

    @Test
    public void getAllFilesByUserIdShouldReturnEmptyFlux() {
        Mockito.when(fileRepository.getAllFilesByUserId(1)).thenReturn(Flux.empty());

        Flux<FileDto> result = fileServiceImpl.getAllFilesByUserId(1);

        StepVerifier.create(result)
                .expectComplete()
                .verify();
        Mockito.verify(fileRepository).getAllFilesByUserId(1);
    }

    @Test
    public void getAllFilesShouldReturnListOfFileDto() {
        FileEntity file2 = FileEntity.builder()
                .id(2)
                .location("location2")
                .status(Status.DELETED)
                .build();
        FileDto fileDto2 = FileDto.builder()
                .id(2)
                .location("location2")
                .status(Status.DELETED)
                .build();

        Mockito.when(fileRepository.findAll()).thenReturn(Flux.just(file, file2));
        Mockito.when(fileMapper.map(file)).thenReturn(fileDto);
        Mockito.when(fileMapper.map(file2)).thenReturn(fileDto2);

        Flux<FileDto> result = fileServiceImpl.getAllFiles();

        StepVerifier.create(result)
                .expectNextMatches(dto -> {
                    return dto.getId() == 1 &&
                            dto.getLocation().equals("location") &&
                            dto.getStatus() == Status.ACTIVE;
                })
                .expectNextMatches(dto -> {
                    return dto.getId() == 2 &&
                            dto.getLocation().equals("location2") &&
                            dto.getStatus() == Status.DELETED;
                })
                .expectComplete()
                .verify();
        Mockito.verify(fileRepository).findAll();
    }

    @Test
    public void getAllFilesShouldReturnEmptyFlux() {
        Mockito.when(fileRepository.findAll()).thenReturn(Flux.empty());

        Flux<FileDto> result = fileServiceImpl.getAllFiles();

        StepVerifier.create(result)
                .expectComplete()
                .verify();
        Mockito.verify(fileRepository).findAll();
    }

    @Test
    public void getAllFilesShouldReturnVoidMono() {
        Mockito.when(fileRepository.deleteFile(1)).thenReturn(Mono.empty());

        Mono<Void> result = fileServiceImpl.deleteFileById(1);

        StepVerifier.create(result)
                .expectComplete()
                .verify();
        Mockito.verify(fileRepository).deleteFile(1);
    }
}
