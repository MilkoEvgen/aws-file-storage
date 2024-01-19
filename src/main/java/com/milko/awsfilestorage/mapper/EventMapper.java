package com.milko.awsfilestorage.mapper;

import com.milko.awsfilestorage.dto.EventDto;
import com.milko.awsfilestorage.dto.FileDto;
import com.milko.awsfilestorage.model.EventEntity;
import com.milko.awsfilestorage.model.FileEntity;
import com.milko.awsfilestorage.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class EventMapper {
    private final FileRepository fileRepository;
    private final FileMapper fileMapper;

    public EventEntity map(EventDto eventDto){
        if ( eventDto == null ) {
            return null;
        }
        return EventEntity.builder()
                .id(eventDto.getId())
                .userId(eventDto.getUser().getId())
                .fileId(eventDto.getFile().getId())
                .status(eventDto.getStatus())
                .build();
    }

    public Mono<EventDto> mapToEventDto(EventEntity event){
        Mono<FileEntity> fileMono = fileRepository.findById(event.getFileId());
        return fileMono.map(
                file -> {
                    FileDto fileDto = fileMapper.map(file);
                    return EventDto.builder()
                            .id(event.getId())
                            .file(fileDto)
                            .status(event.getStatus())
                            .build();
                }
        );
    }
}
