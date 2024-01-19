package com.milko.awsfilestorage.repository;

import com.milko.awsfilestorage.model.EventEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EventRepository extends R2dbcRepository<EventEntity, Integer> {
    @Query("UPDATE events SET status = 'DELETED' WHERE id = :id")
    Mono<Void> deleteEvent(Integer id);

    @Query("UPDATE events SET status = 'DELETED' WHERE user_id = :id")
    Mono<Void> deleteEventByUserId(Integer id);

    @Query("SELECT * FROM events e WHERE e.user_id = :id")
    Flux<EventEntity> getAllEventsByUserId(Integer id);

    @Query("SELECT * FROM events e " +
            "INNER JOIN users u ON e.user_id = u.id " +
            "WHERE u.username = :username")
    Flux<EventEntity> getAllEventsByUsername(String username);

    @Query("SELECT user_id FROM events WHERE file_id = :fileId")
    Mono<Integer> getOwnerIdByFileId(Integer fileId);
}
