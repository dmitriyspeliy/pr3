package com.effectivemobile.practice3.repository;

import com.effectivemobile.practice3.model.entity.Task;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TaskRepository extends R2dbcRepository<Task, Long> {
    Mono<Task> findByTitle(String title);
}
