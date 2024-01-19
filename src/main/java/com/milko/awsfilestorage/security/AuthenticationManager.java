package com.milko.awsfilestorage.security;

import com.milko.awsfilestorage.exception.UnauthorizedException;
import com.milko.awsfilestorage.model.Status;
import com.milko.awsfilestorage.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthenticationManager implements ReactiveAuthenticationManager {

    private final UserServiceImpl userServiceImpl;
    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        return userServiceImpl.getUserById(principal.getId())
                .filter(user -> !user.getStatus().equals(Status.DELETED))
                .switchIfEmpty(Mono.error(new UnauthorizedException("User disabled")))
                .map(user -> authentication);
    }
}
