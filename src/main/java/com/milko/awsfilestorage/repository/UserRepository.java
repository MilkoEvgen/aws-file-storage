package com.milko.awsfilestorage.repository;

import com.milko.awsfilestorage.model.UserEntity;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Mono;

public interface UserRepository extends R2dbcRepository<UserEntity, Integer> {
    Mono<UserEntity> findByUsername(String userName);

    @Query("UPDATE users SET status = 'DELETED' WHERE id = :id")
    Mono<Void> deleteUserById(Integer id);
}
