package com.effectivemobile.practice3.service;

import com.effectivemobile.practice3.mapper.TaskMapper;
import com.effectivemobile.practice3.model.dto.TaskDto;
import com.effectivemobile.practice3.model.entity.Task;
import com.effectivemobile.practice3.repository.impl.TaskRepositoryImpl;
import com.effectivemobile.practice3.utils.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskRepositoryImpl taskRepository;
    private final TaskMapper taskMapper;
    private final String cleanRate = "600000"; //10 mins


    @Cacheable("tasks")
    public Flux<Task> getAllTask(Integer limit, Integer pageOfNumber) {
        log.info("Find all with limit " + limit + " and numberOfPage " + pageOfNumber);
        Integer offset = (pageOfNumber - 1) * limit;
        return taskRepository.findAll(limit, offset);
    }

    @CacheEvict(value = "tasks", allEntries = true)
    @Scheduled(fixedRateString = cleanRate)
    public void emptyTasksCache() {
        log.info("Emptying Tasks cache");
    }

    @Cacheable("task")
    public Mono<Task> findByTaskId(Long taskId) {
        return taskRepository.findById(taskId);
    }

    @CacheEvict(value = "task", allEntries = true)
    @Scheduled(fixedRateString = cleanRate)
    public void emptyTaskCache() {
        log.info("Emptying Task cache");
    }

    @CacheEvict(value = "task", key = "#taskId")
    public Mono<Void> deleteTask(Long taskId) {
        log.info("Delete task by id " + taskId);
        return findByTaskId(taskId)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(optionalTutorial -> {
                    if (optionalTutorial.isEmpty()) {
                        return Mono.error(new BadRequestException("Couldn't find task by task id " + taskId));
                    }
                    return taskRepository.deleteById(taskId);
                });
    }

    public Mono<Task> refreshById(Long taskId, TaskDto taskDto) {
        log.info("Refresh task by id " + taskId);
        return findByTaskId(taskId)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(optionalTutorial -> {
                    if (optionalTutorial.isPresent()) {
                        return taskRepository.updateById(taskId, taskDto);
                    }
                    return Mono.error(new BadRequestException("Couldn't find task by task id " + taskId));
                });
    }

    public Mono<Task> createTask(TaskDto taskDto) {
        log.info("Create new task");
        return taskRepository.findByTitle(taskDto.getTitle())
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(optionalTutorial -> {
                    if (optionalTutorial.isEmpty()) {
                        return taskRepository.save(taskMapper.toEntityTask(taskDto));
                    }
                    return Mono.error(new BadRequestException("Task find by title: " + taskDto.getTitle()));
                });
    }

}
