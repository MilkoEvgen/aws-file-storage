package com.milko.awsfilestorage.repository;

import com.milko.awsfilestorage.model.FileEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface FileRepository extends R2dbcRepository<FileEntity, Integer> {

    @Query("UPDATE files SET status = 'DELETED' WHERE id = :id")
    Mono<Void> deleteFile(Integer id);

    @Query("SELECT * FROM files f " +
            "INNER JOIN events e ON f.id = e.file_id " +
            "WHERE e.user_id = :id")
    Flux<FileEntity> getAllFilesByUserId(Integer id);

    @Query("UPDATE files f " +
            "INNER JOIN events e ON f.id = e.file_id " +
            "SET f.status = 'DELETED' " +
            "WHERE e.id = :id")
    Mono<Void> deleteFileByEventId(Integer id);

    @Query("UPDATE files f " +
            "INNER JOIN events e ON f.id = e.file_id " +
            "SET f.status = 'DELETED' " +
            "WHERE e.user_id = :userId")
    Mono<Void> deleteFileByUserId(Integer userId);
}
