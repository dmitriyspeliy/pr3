package com.effectivemobile.practice3.controller.impl;


import com.effectivemobile.practice3.controller.TaskController;
import com.effectivemobile.practice3.model.dto.TaskDto;
import com.effectivemobile.practice3.model.entity.Task;
import com.effectivemobile.practice3.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Mono<Task>> create(@RequestBody TaskDto taskDto) {
        return ResponseEntity.ok(taskService.createTask(taskDto));
    }

    @GetMapping
    public Flux<Task> getAllTask() {
        return taskService.getAllTask();
    }

    @Override
    @PostMapping("/update/{id}")
    public ResponseEntity<Mono<Task>> update(@PathVariable(name = "id") Long id,
                                             @RequestBody TaskDto taskDto) {
        return ResponseEntity.ok(taskService.refreshById(id, taskDto));
    }

    @Override
    @GetMapping("{id}")
    public Mono<Void> deleteTaskById(@PathVariable(name = "id") Long id) {
        return taskService.deleteTask(id);
    }
}
