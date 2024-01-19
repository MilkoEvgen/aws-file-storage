package com.milko.awsfilestorage.rest;

import com.milko.awsfilestorage.dto.AuthRequestDto;
import com.milko.awsfilestorage.dto.AuthResponseDto;
import com.milko.awsfilestorage.dto.UserDto;
import com.milko.awsfilestorage.security.CustomPrincipal;
import com.milko.awsfilestorage.security.SecurityService;
import com.milko.awsfilestorage.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthRestControllerV1 {

    private final SecurityService securityService;
    private final UserService userService;


    @PostMapping("/register")
    public Mono<UserDto> register(@RequestBody UserDto userDto){
        return userService.registerUser(userDto);
    }

    @PostMapping("/login")
    public Mono<AuthResponseDto> login(@RequestBody AuthRequestDto dto){
        return securityService.authenticate(dto.getUsername(), dto.getPassword())
                .flatMap(tokenDetails -> Mono.just(
                        AuthResponseDto.builder()
                                .userId(tokenDetails.getUserId())
                                .token(tokenDetails.getToken())
                                .issuedAt(tokenDetails.getIssuedAt())
                                .expiresAt(tokenDetails.getExpiresAt())
                                .build()
                ));
    }

    @GetMapping("/info")
    public Mono<UserDto> getUserInfo(Authentication authentication){
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        return userService.getUserById(principal.getId());
    }
}
