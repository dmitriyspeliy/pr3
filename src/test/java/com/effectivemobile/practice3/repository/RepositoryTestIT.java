package com.effectivemobile.practice3.repository;

import com.effectivemobile.practice3.configDB_IT.ConfigDB;
import com.effectivemobile.practice3.model.dto.TaskDto;
import com.effectivemobile.practice3.model.entity.Task;
import com.effectivemobile.practice3.repository.impl.TaskRepositoryImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class RepositoryTestIT extends ConfigDB {

    @Autowired
    private TaskRepositoryImpl taskRepository;

    private Task task1;
    private Task task2;
    private Task task3;
    private Task task4;
    private TaskDto taskDto;

    @BeforeEach
    void inti() {
        task1 = new Task();
        task1.setDescription("Description 1");
        task1.setTitle("Title 1");
        task2 = new Task();
        task2.setDescription("Description 2");
        task2.setTitle("Title 2");
        task3 = new Task();
        task3.setDescription("Description 3");
        task3.setTitle("Title 3");
        task4 = new Task();
        task4.setDescription("Description 4");
        task4.setTitle("Title 4");
        taskRepository.save(task1).block();
        taskRepository.save(task3).block();
        taskRepository.save(task2).block();
        taskDto = new TaskDto();
        taskDto.setTitle("New title");
        taskDto.setDescription("New description");
    }

    @AfterEach
    void init() {
        taskRepository.deleteAll().block();
        task1 = null;
        task2 = null;
        task3 = null;
        task4 = null;
        taskDto = null;
    }

    @DisplayName(value = "Find by id")
    @Test
    @Order(1)
    void when_invokeMethodFindByID_return_Task() {
        assertNotNull(taskRepository);
        Task task = taskRepository.findById(1L).block();
        assertNotNull(task);
    }

    @DisplayName(value = "Delete task")
    @Test
    @Order(2)
    void when_invokeMethodDeleteTask_return_Task() {
        assertNotNull(taskRepository);
        Task task = taskRepository.findByTitle("Title 1").block();
        taskRepository.deleteById(task.getId()).block();
        assertEquals(2, Objects.requireNonNull(taskRepository.findAll(10, 0).collectList().block()).size());
    }

    @DisplayName(value = "Find by title")
    @Test
    @Order(3)
    void when_invokeMethodFindByTitle_return_Task() {
        assertNotNull(taskRepository);
        Task task = taskRepository.findByTitle("Title 1").block();
        assertNotNull(task);
    }

    @DisplayName(value = "Save task")
    @Test
    @Order(4)
    void when_invokeMethodSaveTask_return_Task() {
        assertNotNull(taskRepository);
        Mono<Task> save = taskRepository.save(task4);
        Task task = taskRepository.findById(Objects.requireNonNull(save.block()).getId()).block();
        assertNotNull(task);
    }

    @DisplayName(value = "Delete all")
    @Test
    @Order(5)
    void when_invokeMethodDeleteAll_return_Task() {
        assertNotNull(taskRepository);
        taskRepository.deleteAll().block();
        assertEquals(0, Objects.requireNonNull(taskRepository.findAll(10, 0).collectList().block()).size());
    }

    @DisplayName(value = "Update by id")
    @Test
    @Order(5)
    void when_invokeMethodUpdateById_return_Task() {
        assertNotNull(taskRepository);
        Task task = taskRepository.findByTitle("Title 1").block();
        taskRepository.updateById(task.getId(), taskDto).block();
        Mono<Task> newTask = taskRepository.findById(task.getId());
        assertEquals(taskDto.getTitle(), Objects.requireNonNull(newTask.block()).getTitle());
    }

}
