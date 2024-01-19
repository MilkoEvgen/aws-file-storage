package com.milko.awsfilestorage.rest;

import com.milko.awsfilestorage.dto.FileDto;
import com.milko.awsfilestorage.model.Status;
import com.milko.awsfilestorage.security.CustomPrincipal;
import com.milko.awsfilestorage.service.EventService;
import com.milko.awsfilestorage.service.FileService;
import com.milko.awsfilestorage.service.StorageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.codec.multipart.FilePart;
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
@RequestMapping("/api/v1/files")
public class FileRestControllerV1 {
    private final FileService fileService;
    private final EventService eventService;
    private final StorageService storageService;

    @PostMapping("/upload")
    public Mono<FileDto> uploadFile(@RequestPart("file") FilePart filePart, Authentication authentication) {
        CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
        Integer userId = principal.getId();

        return storageService.uploadFile(filePart)
                .flatMap(location -> {
                    FileDto fileDto = FileDto.builder()
                            .location(location)
                            .status(Status.ACTIVE)
                            .build();
                    return fileService.createFile(fileDto, userId);
                });
    }

    @GetMapping("/{fileId}")
    public Mono<FileDto> getFileById(@PathVariable Integer fileId, Authentication authentication) {
        if (userHaveRole(authentication, "ROLE_ADMIN") || userHaveRole(authentication, "ROLE_MODERATOR")) {
            return fileService.getFileById(fileId);
        } else if (userHaveRole(authentication, "ROLE_USER")) {
            CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
            Mono<Integer> ownerIdMono = eventService.getOwnerIdByFileId(fileId);
            return ownerIdMono.flatMap(
                    ownerId -> {
                        if (Objects.equals(principal.getId(), ownerId)){
                            return fileService.getFileById(fileId);
                        } else {
                            return Mono.error(new AccessDeniedException("Access denied"));
                        }
                    }
            );
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }


    @GetMapping("/user/{userId}")
    public Flux<FileDto> getAllFilesByUserId(@PathVariable Integer userId, Authentication authentication) {
        if (userHaveRole(authentication, "ROLE_ADMIN") || userHaveRole(authentication, "ROLE_MODERATOR")) {
            return fileService.getAllFilesByUserId(userId);
        } else if (userHaveRole(authentication, "ROLE_USER")) {
            CustomPrincipal principal = (CustomPrincipal) authentication.getPrincipal();
            if (Objects.equals(principal.getId(), userId)) {
                return fileService.getAllFilesByUserId(userId);
            } else {
                throw new AccessDeniedException("Access denied");
            }
        } else {
            throw new AccessDeniedException("Access denied");
        }
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public Flux<FileDto> getAllFiles() {
        return fileService.getAllFiles();
    }

    @PatchMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public Mono<FileDto> updateFile(@RequestBody FileDto fileDto) {
        return fileService.updateFile(fileDto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MODERATOR')")
    public Mono<Void> deleteFileById(@PathVariable Integer id) {
        return fileService.deleteFileById(id);
    }

    private boolean userHaveRole(Authentication authentication, String role) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        for (GrantedAuthority authority : authorities) {
            if (authority.getAuthority().equals(role)) {
                return true;
            }
        }
        return false;
    }
}
