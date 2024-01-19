package com.milko.awsfilestorage.service.impl;

import com.milko.awsfilestorage.dto.EventDto;
import com.milko.awsfilestorage.mapper.EventMapper;
import com.milko.awsfilestorage.model.EventEntity;
import com.milko.awsfilestorage.repository.EventRepository;
import com.milko.awsfilestorage.repository.FileRepository;
import com.milko.awsfilestorage.service.EventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final FileRepository fileRepository;
    private final EventMapper eventMapper;

    public Mono<EventDto> updateEvent(EventDto eventDto){
        log.info("IN EventService updateEvent(), eventDto: {}", eventDto);
        EventEntity event = eventMapper.map(eventDto);
        return eventRepository.save(event)
                .flatMap(eventMapper::mapToEventDto);
    }

    public Mono<EventDto> getEventById(Integer id){
        log.info("IN EventService getEventById(), id: {}", id);
        return eventRepository.findById(id)
                .flatMap(eventMapper::mapToEventDto);
    }

    public Flux<EventDto> getAllEventsByUserId(Integer id) {
        log.info("IN EventService getAllEventsByUserId(), id: {}", id);
        return eventRepository.getAllEventsByUserId(id)
                .flatMap(eventMapper::mapToEventDto);
    }

    public Flux<EventDto> getAllEvents(){
        log.info("IN EventService getAllEvents()");
        return eventRepository.findAll()
                .flatMap(eventMapper::mapToEventDto);
    }

    public Mono<Void> deleteEventById(Integer id){
        log.info("IN EventService deleteEventById(), id: {}", id);
        return fileRepository.deleteFileByEventId(id)
                .then(eventRepository.deleteEvent(id));
    }

    public Mono<Integer> getOwnerIdByFileId(Integer fileId){
        return eventRepository.getOwnerIdByFileId(fileId);
    }



}
