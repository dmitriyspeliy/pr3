package com.effectivemobile.practice3.controller.impl;


import com.effectivemobile.practice3.controller.TaskController;
import com.effectivemobile.practice3.model.dto.TaskDto;
import com.effectivemobile.practice3.model.entity.Task;
import com.effectivemobile.practice3.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/api")
public class TaskControllerImpl implements TaskController {

    private final TaskService taskService;

    @Override
    @PostMapping
    public Mono<Task> create(@RequestBody TaskDto taskDto) {
        return taskService.createTask(taskDto);
    }

    @Override
    @GetMapping
    public Flux<Task> getAllTask(@RequestParam(name = "limit", defaultValue = "10") Integer limit,
                                 @RequestParam(name = "numberOfPage", defaultValue = "1") Integer numberOfPage) {
        return taskService.getAllTask(limit, numberOfPage);
    }

    @Override
    @PutMapping("/update/{id}")
    public Mono<Task> update(@PathVariable(name = "id") Long id,
                             @RequestBody TaskDto taskDto) {
        return taskService.refreshById(id, taskDto);
    }

    @Override
    @DeleteMapping("{id}")
    public Mono<Void> deleteTaskById(@PathVariable(name = "id") Long id) {
        return taskService.deleteTask(id);
    }
}
