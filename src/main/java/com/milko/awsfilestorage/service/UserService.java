package com.milko.awsfilestorage.service;

import com.milko.awsfilestorage.dto.UserDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserService {
    Mono<UserDto> registerUser(UserDto userDto);

    Mono<UserDto> getUserById(Integer id);

    Mono<UserDto> getUserByUsername(String username);

    Flux<UserDto> getAllUsers();

    Mono<Void> deleteUserById(Integer id);

    Mono<UserDto> updateUser(UserDto userDto);
}
