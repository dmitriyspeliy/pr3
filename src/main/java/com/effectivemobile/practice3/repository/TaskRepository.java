package com.effectivemobile.practice3.repository;

import com.effectivemobile.practice3.model.dto.TaskDto;
import com.effectivemobile.practice3.model.entity.Task;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskRepository<T> {

    Mono<T> findByTitle(String title);

    Mono<T> save(T task);

    Mono<Void> deleteById(Long id);

    Mono<T> findById(Long id);

    Flux<T> findAll();

    Mono<Task> updateById(Long id, TaskDto taskDto);

}
