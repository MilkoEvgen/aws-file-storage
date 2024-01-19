package com.milko.awsfilestorage.service;

import com.milko.awsfilestorage.dto.EventDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EventService {
    Mono<EventDto> updateEvent(EventDto eventDto);

    Mono<EventDto> getEventById(Integer id);

    Flux<EventDto> getAllEventsByUserId(Integer id) ;

    Flux<EventDto> getAllEvents();

    Mono<Void> deleteEventById(Integer id);

    Mono<Integer> getOwnerIdByFileId(Integer fileId);


}
