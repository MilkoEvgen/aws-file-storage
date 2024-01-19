package com.milko.awsfilestorage.service.impl;

import com.milko.awsfilestorage.dto.EventDto;
import com.milko.awsfilestorage.dto.UserDto;
import com.milko.awsfilestorage.mapper.EventMapper;
import com.milko.awsfilestorage.mapper.UserMapper;
import com.milko.awsfilestorage.model.Status;
import com.milko.awsfilestorage.model.UserEntity;
import com.milko.awsfilestorage.repository.EventRepository;
import com.milko.awsfilestorage.repository.FileRepository;
import com.milko.awsfilestorage.repository.UserRepository;
import com.milko.awsfilestorage.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final FileRepository fileRepository;
    private final PasswordEncoder passwordEncoder;
    private final EventMapper eventMapper;
    private final UserMapper userMapper;

    public Mono<UserDto> registerUser(UserDto userDto) {
        log.info("IN UserService registerUser(), userDto: {}", userDto);
        UserEntity userEntity = userMapper.map(userDto);
        return userRepository.save(
                        userEntity.toBuilder()
                                .password(passwordEncoder.encode(userEntity.getPassword()))
                                .status(Status.ACTIVE)
                                .build()
                ).map(userMapper::map);
    }


    public Mono<UserDto> getUserById(Integer id) {
        log.info("IN UserService getUserById(), id: {}", id);
        Mono<UserDto> userDtoMono = userRepository.findById(id)
                .map(userMapper::map);

        Flux<EventDto> eventDtoFlux = eventRepository.getAllEventsByUserId(id)
                .flatMap(eventMapper::mapToEventDto);

        return userDtoMono.flatMap(userDto ->
                eventDtoFlux.collectList()
                        .map(eventDtos -> {
                            userDto.setEvents(eventDtos);
                            return userDto;
                        })
        );
    }

    public Mono<UserDto> getUserByUsername(String username) {
        log.info("IN UserService getUserByUsername(), username: {}", username);
        Mono<UserDto> userDtoMono = userRepository.findByUsername(username)
                .map(userMapper::map);

        Flux<EventDto> eventDtoFlux = eventRepository.getAllEventsByUsername(username)
                .flatMap(eventMapper::mapToEventDto);

        return userDtoMono.flatMap(userDto ->
                eventDtoFlux.collectList()
                        .map(eventDtos -> {
                            userDto.setEvents(eventDtos);
                            return userDto;
                        })
        );
    }

    public Flux<UserDto> getAllUsers() {
        log.info("IN UserService getAllUsers()");
        return userRepository.findAll()
                .map(userMapper::map);
    }

    public Mono<Void> deleteUserById(Integer id) {
        log.info("IN UserService deleteUserById(), id: {}", id);
        return eventRepository.deleteEventByUserId(id)
                .then(fileRepository.deleteFileByUserId(id))
                .then(userRepository.deleteUserById(id));
    }

    public Mono<UserDto> updateUser(UserDto userDto) {
        log.info("IN UserService updateUser(), userDto: {}", userDto);
        UserEntity userEntity = userMapper.map(userDto);
        return userRepository.save(
                userEntity.toBuilder()
                        .password(passwordEncoder.encode(userEntity.getPassword()))
                        .build()
        ).map(userMapper::map);
    }
}
