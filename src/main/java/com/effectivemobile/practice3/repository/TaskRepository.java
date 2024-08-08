package com.effectivemobile.practice3.repository;

import com.effectivemobile.practice3.model.dto.TaskDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TaskRepository<T> {

    Mono<T> findByTitle(String title);

    Mono<T> save(T task);

    Mono<Void> deleteById(Long id);

    Mono<T> findById(Long id);

    Flux<T> findAll(Integer limit, Integer offset);

    Mono<Long> updateById(Long id, TaskDto taskDto);

}
