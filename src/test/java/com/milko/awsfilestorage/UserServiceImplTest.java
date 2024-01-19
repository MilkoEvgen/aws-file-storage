package com.milko.awsfilestorage;

import com.milko.awsfilestorage.dto.EventDto;
import com.milko.awsfilestorage.dto.FileDto;
import com.milko.awsfilestorage.dto.UserDto;
import com.milko.awsfilestorage.mapper.EventMapper;
import com.milko.awsfilestorage.mapper.FileMapper;
import com.milko.awsfilestorage.mapper.UserMapper;
import com.milko.awsfilestorage.model.*;
import com.milko.awsfilestorage.repository.EventRepository;
import com.milko.awsfilestorage.repository.FileRepository;
import com.milko.awsfilestorage.repository.UserRepository;
import com.milko.awsfilestorage.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userServiceImpl;
    @Mock
    private UserRepository userRepository;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private FileRepository fileRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;
    @Mock
    private FileMapper fileMapper;
    @Mock
    private EventMapper eventMapper;

    private EventEntity event;
    private EventDto eventDto;
    private FileEntity file;
    private FileDto fileDto;
    private UserDto userInputDto;
    private UserDto userOutputDto;
    private UserEntity userInput;
    private UserEntity userOutput;

    @BeforeEach
    public void init() {
        userInputDto = UserDto.builder()
                .username("username")
                .password("rawPassword")
                .name("name")
                .role(UserRole.ROLE_USER)
                .status(Status.ACTIVE)
                .build();
        userOutputDto = UserDto.builder()
                .id(1)
                .username("username")
                .password("encodedPassword")
                .name("name")
                .role(UserRole.ROLE_USER)
                .status(Status.ACTIVE)
                .build();
        userInput = UserEntity.builder()
                .username("username")
                .password("rawPassword")
                .name("name")
                .role(UserRole.ROLE_USER)
                .status(Status.ACTIVE)
                .build();
        userOutput = UserEntity.builder()
                .id(1)
                .username("username")
                .password("encodedPassword")
                .name("name")
                .role(UserRole.ROLE_USER)
                .status(Status.ACTIVE)
                .build();
        event = EventEntity.builder()
                .id(1)
                .userId(1)
                .fileId(1)
                .status(Status.ACTIVE)
                .build();
        eventDto = EventDto.builder()
                .id(1)
                .user(userOutputDto)
                .file(fileDto)
                .build();
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
    }

    @Test
    public void registerUserShouldReturnUser() {
        Mockito.when(userMapper.map(any(UserDto.class))).thenReturn(userInput);
        Mockito.when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        Mockito.when(userRepository.save(any(UserEntity.class))).thenReturn(Mono.just(userOutput));
        Mockito.when(userMapper.map(any(UserEntity.class))).thenReturn(userOutputDto);

        Mono<UserDto> result = userServiceImpl.registerUser(userInputDto);

        StepVerifier.create(result)
                .expectNextMatches(userDto -> {
                    return userDto.getId() == 1 &&
                            userDto.getUsername().equals("username") &&
                            userDto.getPassword().equals("encodedPassword") &&
                            userDto.getName().equals("name") &&
                            userDto.getRole().equals(UserRole.ROLE_USER) &&
                            userDto.getStatus().equals(Status.ACTIVE);
                }).expectComplete()
                .verify();
        Mockito.verify(userMapper).map(any(UserDto.class));
        Mockito.verify(userMapper).map(any(UserEntity.class));
        Mockito.verify(passwordEncoder).encode(any(String.class));
        Mockito.verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    public void getUserByIdShouldReturnUser(){
        Mockito.when(userRepository.findById(any(Integer.class))).thenReturn(Mono.just(userOutput));
        Mockito.when(userMapper.map(any(UserEntity.class))).thenReturn(userOutputDto);
        Mockito.when(eventRepository.getAllEventsByUserId(any(Integer.class))).thenReturn(Flux.just(event));
        Mockito.when(eventMapper.mapToEventDto(any(EventEntity.class))).thenReturn(Mono.just(eventDto));

        Mono<UserDto> result = userServiceImpl.getUserById(1);

        StepVerifier.create(result)
                .expectNextMatches(userDto -> {
                    return userDto.getId() == 1 &&
                            userDto.getUsername().equals("username") &&
                            userDto.getPassword().equals("encodedPassword") &&
                            userDto.getName().equals("name") &&
                            userDto.getRole().equals(UserRole.ROLE_USER) &&
                            userDto.getStatus().equals(Status.ACTIVE);
                }).expectComplete()
                .verify();

        Mockito.verify(userRepository).findById(any(Integer.class));
        Mockito.verify(userMapper).map(any(UserEntity.class));
        Mockito.verify(eventRepository).getAllEventsByUserId(any(Integer.class));
        Mockito.verify(eventMapper).mapToEventDto(any(EventEntity.class));
    }

    @Test
    public void getUserByUsernameShouldReturnUser(){
        Mockito.when(userRepository.findByUsername(any(String.class))).thenReturn(Mono.just(userOutput));
        Mockito.when(userMapper.map(any(UserEntity.class))).thenReturn(userOutputDto);
        Mockito.when(eventRepository.getAllEventsByUsername(any(String.class))).thenReturn(Flux.just(event));
        Mockito.when(eventMapper.mapToEventDto(any(EventEntity.class))).thenReturn(Mono.just(eventDto));

        Mono<UserDto> result = userServiceImpl.getUserByUsername("username");

        StepVerifier.create(result)
                .expectNextMatches(userDto -> {
                    return userDto.getId() == 1 &&
                            userDto.getUsername().equals("username") &&
                            userDto.getPassword().equals("encodedPassword") &&
                            userDto.getName().equals("name") &&
                            userDto.getRole().equals(UserRole.ROLE_USER) &&
                            userDto.getStatus().equals(Status.ACTIVE);
                }).expectComplete()
                .verify();

        Mockito.verify(userRepository).findByUsername(any(String.class));
        Mockito.verify(userMapper).map(any(UserEntity.class));
        Mockito.verify(eventRepository).getAllEventsByUsername(any(String.class));
        Mockito.verify(eventMapper).mapToEventDto(any(EventEntity.class));
    }

    @Test
    public void getAllUsersShouldReturnListOfUsers(){
        Mockito.when(userRepository.findAll()).thenReturn(Flux.just(userOutput));
        Mockito.when(userMapper.map(any(UserEntity.class))).thenReturn(userOutputDto);

        Flux<UserDto> result = userServiceImpl.getAllUsers();

        StepVerifier.create(result)
                .expectNextMatches(userDto -> {
                    return userDto.getId() == 1 &&
                            userDto.getUsername().equals("username") &&
                            userDto.getPassword().equals("encodedPassword") &&
                            userDto.getName().equals("name") &&
                            userDto.getRole().equals(UserRole.ROLE_USER) &&
                            userDto.getStatus().equals(Status.ACTIVE);
                }).expectComplete()
                .verify();

        Mockito.verify(userRepository).findAll();
        Mockito.verify(userMapper).map(any(UserEntity.class));
    }

    @Test
    public void getAllUsersShouldReturnEmptyFlux(){
        Mockito.when(userRepository.findAll()).thenReturn(Flux.empty());

        Flux<UserDto> result = userServiceImpl.getAllUsers();

        StepVerifier.create(result)
                .expectComplete()
                .verify();

        Mockito.verify(userRepository).findAll();
    }

    @Test
    public void deleteUserByIdShouldReturnMonoOfVoid(){
        Mockito.when(eventRepository.deleteEventByUserId(any(Integer.class))).thenReturn(Mono.empty());
        Mockito.when(fileRepository.deleteFileByUserId(any(Integer.class))).thenReturn(Mono.empty());
        Mockito.when(userRepository.deleteUserById(any(Integer.class))).thenReturn(Mono.empty());

        Mono<Void> result = userServiceImpl.deleteUserById(1);

        StepVerifier.create(result)
                .expectComplete()
                .verify();

        Mockito.verify(eventRepository).deleteEventByUserId(any(Integer.class));
        Mockito.verify(fileRepository).deleteFileByUserId(any(Integer.class));
        Mockito.verify(userRepository).deleteUserById(any(Integer.class));
    }

    @Test
    public void updateUserShouldReturnUserDtoMono(){
        Mockito.when(userMapper.map(any(UserDto.class))).thenReturn(userInput);
        Mockito.when(userRepository.save(any(UserEntity.class))).thenReturn(Mono.just(userOutput));
        Mockito.when(passwordEncoder.encode("rawPassword")).thenReturn("encodedPassword");
        Mockito.when(userMapper.map(any(UserEntity.class))).thenReturn(userOutputDto);

        Mono<UserDto> result = userServiceImpl.updateUser(userInputDto);

        StepVerifier.create(result)
                .expectNextMatches(userDto -> {
                    return userDto.getId() == 1 &&
                            userDto.getUsername().equals("username") &&
                            userDto.getPassword().equals("encodedPassword") &&
                            userDto.getName().equals("name") &&
                            userDto.getRole().equals(UserRole.ROLE_USER) &&
                            userDto.getStatus().equals(Status.ACTIVE);
                }).expectComplete()
                .verify();

        Mockito.verify(userMapper).map(any(UserEntity.class));
        Mockito.verify(userMapper).map(any(UserDto.class));
        Mockito.verify(userRepository).save(any(UserEntity.class));
        Mockito.verify(passwordEncoder).encode(any(String.class));
    }
}
