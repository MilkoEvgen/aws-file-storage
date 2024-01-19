package com.milko.awsfilestorage.rest;

import com.milko.awsfilestorage.dto.EventDto;
import com.milko.awsfilestorage.security.CustomPrincipal;
import com.milko.awsfilestorage.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collection;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/events")
public class EventRestControllerV1 {

    private final EventService eventService;


    @GetMapping("/{id}")
    public Mono<EventDto> getEventById(@PathVariable Integer id, Authentication authentication){
        if (userHaveRole(authentication, "ROLE_ADMIN") || userHaveRole(authentication, "ROLE_MODERATOR")){
            return eventService.getEventById(id);
        } else if (userHaveRole(authentication, "ROLE_USER")) {
            CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
            if (Objects.equals(principal.getId(), id)){
                return eventService.getEventById(id);
            } else {
                throw new AccessDeniedException("Access denied");
            }
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }

    @GetMapping("/userid/{id}")
    public Flux<EventDto> getAllEventsByUserId(@PathVariable Integer id, Authentication authentication){
        if (userHaveRole(authentication, "ROLE_ADMIN") || userHaveRole(authentication, "ROLE_MODERATOR")){
            return eventService.getAllEventsByUserId(id);
        } else if (userHaveRole(authentication, "ROLE_USER")) {
            CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
            if (Objects.equals(principal.getId(), id)){
                return eventService.getAllEventsByUserId(id);
            } else {
                throw new AccessDeniedException("Access denied");
            }
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public Flux<EventDto> getAllEvents(){
        return eventService.getAllEvents();
    }

    @PatchMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public Mono<EventDto> updateEvent(@RequestBody EventDto eventDto){
        return eventService.updateEvent(eventDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public Mono<Void> deleteEvent(@PathVariable Integer id){
        return eventService.deleteEventById(id);
    }

    private boolean userHaveRole(Authentication authentication, String role){
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals(role)){
                return true;
            }
        }
        return false;
    }

}
