package com.milko.awsfilestorage.rest;

import com.milko.awsfilestorage.dto.UserDto;
import com.milko.awsfilestorage.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserRestControllerV1 {

    private final UserService userService;


    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public Mono<UserDto> getUserById(@PathVariable Integer id){
        return userService.getUserById(id);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public Flux<UserDto> getAllUsers(){
        return userService.getAllUsers();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Mono<Void> deleteUserById(@PathVariable Integer id){
        return userService.deleteUserById(id);
    }

    @PatchMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Mono<UserDto> updateUser(@RequestBody UserDto userDto){
        return userService.updateUser(userDto);
    }
}

