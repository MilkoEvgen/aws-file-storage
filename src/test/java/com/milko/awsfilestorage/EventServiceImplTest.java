package com.milko.awsfilestorage;

import com.milko.awsfilestorage.dto.EventDto;
import com.milko.awsfilestorage.dto.FileDto;
import com.milko.awsfilestorage.dto.UserDto;
import com.milko.awsfilestorage.mapper.EventMapper;
import com.milko.awsfilestorage.model.EventEntity;
import com.milko.awsfilestorage.model.FileEntity;
import com.milko.awsfilestorage.model.Status;
import com.milko.awsfilestorage.repository.EventRepository;
import com.milko.awsfilestorage.repository.FileRepository;
import com.milko.awsfilestorage.service.impl.EventServiceImpl;
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
public class EventServiceImplTest {
    @Mock
    private EventRepository eventRepository;
    @Mock
    private FileRepository fileRepository;
    @Mock
    private EventMapper eventMapper;
    @InjectMocks
    private EventServiceImpl eventServiceImpl;

    private EventDto eventDto;
    private EventEntity event;
    private FileDto fileDto;
    private FileEntity file;
    private UserDto userDto;


    @BeforeEach
    public void init() {
        userDto = UserDto.builder()
                .id(1)
                .username("username")
                .build();
        fileDto = FileDto.builder()
                .id(1)
                .location("location")
                .status(Status.ACTIVE)
                .build();
        eventDto = EventDto.builder()
                .id(1)
                .user(userDto)
                .file(fileDto)
                .status(Status.ACTIVE)
                .build();
        event = EventEntity.builder()
                .id(1)
                .userId(1)
                .fileId(1)
                .status(Status.ACTIVE)
                .build();
        file = FileEntity.builder()
                .id(1)
                .location("location")
                .status(Status.ACTIVE)
                .build();
    }

    @Test
    public void updateEventShouldReturnEventDtoMono() {
        Mockito.when(eventMapper.map(any(EventDto.class))).thenReturn(event);
        Mockito.when(eventRepository.save(any(EventEntity.class))).thenReturn(Mono.just(event));
        Mockito.when(eventMapper.mapToEventDto(any(EventEntity.class))).thenReturn(Mono.just(eventDto));

        Mono<EventDto> result = eventServiceImpl.updateEvent(eventDto);

        StepVerifier.create(result)
                .expectNextMatches(dto -> {
                    return dto.getId() == 1 &&
                            dto.getUser().getId() == 1 &&
                            dto.getUser().getUsername().equals("username") &&
                            dto.getFile().getId() == 1 &&
                            dto.getFile().getLocation().equals("location") &&
                            dto.getFile().getStatus().equals(Status.ACTIVE) &&
                            dto.getStatus().equals(Status.ACTIVE);
                }).expectComplete()
                .verify();

        Mockito.verify(eventMapper).map(any(EventDto.class));
        Mockito.verify(eventRepository).save(any(EventEntity.class));
        Mockito.verify(eventMapper).mapToEventDto(any(EventEntity.class));
    }

    @Test
    public void getEventByIdShouldReturnEventDtoMono() {
        Mockito.when(eventRepository.findById(any(Integer.class))).thenReturn(Mono.just(event));
        Mockito.when(eventMapper.mapToEventDto(any(EventEntity.class))).thenReturn(Mono.just(eventDto));

        Mono<EventDto> result = eventServiceImpl.getEventById(1);

        StepVerifier.create(result)
                .expectNextMatches(dto -> {
            return dto.getId() == 1 &&
                    dto.getUser().getId() == 1 &&
                    dto.getUser().getUsername().equals("username") &&
                    dto.getFile().getId() == 1 &&
                    dto.getFile().getLocation().equals("location") &&
                    dto.getFile().getStatus().equals(Status.ACTIVE) &&
                    dto.getStatus().equals(Status.ACTIVE);
        }).expectComplete()
                .verify();

        Mockito.verify(eventRepository).findById(any(Integer.class));
        Mockito.verify(eventMapper).mapToEventDto(any(EventEntity.class));
    }

    @Test
    public void getAllEventsByUserIdShouldReturnFluxOfEventDto(){
        Mockito .when(eventRepository.getAllEventsByUserId(any(Integer.class))).thenReturn(Flux.just(event));
        Mockito.when(eventMapper.mapToEventDto(any(EventEntity.class))).thenReturn(Mono.just(eventDto));

        Flux<EventDto> result = eventServiceImpl.getAllEventsByUserId(1);

        StepVerifier.create(result)
                .expectNextMatches(dto -> {
                    return dto.getId() == 1 &&
                            dto.getUser().getId() == 1 &&
                            dto.getUser().getUsername().equals("username") &&
                            dto.getFile().getId() == 1 &&
                            dto.getFile().getLocation().equals("location") &&
                            dto.getFile().getStatus().equals(Status.ACTIVE) &&
                            dto.getStatus().equals(Status.ACTIVE);
                }).expectComplete()
                .verify();

        Mockito.verify(eventRepository).getAllEventsByUserId(any(Integer.class));
        Mockito.verify(eventMapper).mapToEventDto(any(EventEntity.class));
    }

    @Test
    public void getAllEventsShouldReturnFluxOfEventDto(){
        Mockito .when(eventRepository.findAll()).thenReturn(Flux.just(event));
        Mockito.when(eventMapper.mapToEventDto(any(EventEntity.class))).thenReturn(Mono.just(eventDto));

        Flux<EventDto> result = eventServiceImpl.getAllEvents();

        StepVerifier.create(result)
                .expectNextMatches(dto -> {
                    return dto.getId() == 1 &&
                            dto.getUser().getId() == 1 &&
                            dto.getUser().getUsername().equals("username") &&
                            dto.getFile().getId() == 1 &&
                            dto.getFile().getLocation().equals("location") &&
                            dto.getFile().getStatus().equals(Status.ACTIVE) &&
                            dto.getStatus().equals(Status.ACTIVE);
                }).expectComplete()
                .verify();

        Mockito.verify(eventRepository).findAll();
        Mockito.verify(eventMapper).mapToEventDto(any(EventEntity.class));
    }

    @Test
    public void deleteEventByIdShouldReturnEmptyMono(){
        Mockito.when(fileRepository.deleteFileByEventId(any(Integer.class))).thenReturn(Mono.empty());
        Mockito .when(eventRepository.deleteEvent(any(Integer.class))).thenReturn(Mono.empty());

        Mono<Void> result = eventServiceImpl.deleteEventById(1);

        StepVerifier.create(result)
                .expectComplete()
                .verify();

        Mockito.verify(fileRepository).deleteFileByEventId(any(Integer.class));
        Mockito.verify(eventRepository).deleteEvent(any(Integer.class));
    }

}
