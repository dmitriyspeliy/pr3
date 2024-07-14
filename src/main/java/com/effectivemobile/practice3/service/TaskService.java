package com.effectivemobile.practice3.service;

import com.effectivemobile.practice3.mapper.TaskMapper;
import com.effectivemobile.practice3.model.dto.TaskDto;
import com.effectivemobile.practice3.model.entity.Task;
import com.effectivemobile.practice3.repository.TaskRepository;
import com.effectivemobile.practice3.utils.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;

    public Flux<Task> getAllTask() {
        return taskRepository.findAll();
    }

    public Mono<Task> findByTaskId(Long taskId) {
        return taskRepository.findById(taskId);
    }

    public Mono<Void> deleteTask(Long taskId) {
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
        return findByTaskId(taskId)
                .map(Optional::of)
                .defaultIfEmpty(Optional.empty())
                .flatMap(optionalTutorial -> {
                    if (optionalTutorial.isPresent()) {
                        Task entityTask = taskMapper.toEntityTask(taskDto);
                        entityTask.setId(taskId);
                        return taskRepository.save(entityTask);
                    }
                    return Mono.error(new BadRequestException("Couldn't find task by task id " + taskId));
                });
    }

    public Mono<Task> findByTitle(String title) {
        return taskRepository.findByTitle(title);
    }

    public Mono<Task> createTask(TaskDto taskDto) {
        return findByTitle(taskDto.getTitle())
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
