package com.effectivemobile.practice3.controller.impl;


import com.effectivemobile.practice3.controller.TaskController;
import com.effectivemobile.practice3.model.dto.TaskDto;
import com.effectivemobile.practice3.service.TaskService;
import com.effectivemobile.practice3.utils.exception.BadRequestException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api")
public class TaskControllerImpl implements TaskController {

    private final TaskService taskService;

    public TaskControllerImpl(TaskService taskService) {
        this.taskService = taskService;
    }

    @Override
    @PostMapping
    public ResponseEntity<TaskDto> create(@RequestBody TaskDto taskDto) throws BadRequestException {
        return ResponseEntity.ok(taskService.createTask(taskDto));
    }

    @GetMapping
    public List<TaskDto> getAllTask() {
        return taskService.getAllTask();
    }

    @Override
    @PostMapping("/update/{id}")
    public ResponseEntity<TaskDto> update(@PathVariable(name = "id") Long id,
                                          @RequestBody TaskDto taskDto) throws BadRequestException {
        return ResponseEntity.ok(taskService.refreshById(id, taskDto));
    }

    @Override
    @GetMapping("{id}")
    public void deleteTaskById(@PathVariable(name = "id") Long id) throws BadRequestException {
        taskService.deleteTask(id);
    }
}
