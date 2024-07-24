package com.effectivemobile.practice3.controller.impl;

import com.effectivemobile.practice3.configDB_IT.ConfigDB;
import com.effectivemobile.practice3.model.dto.TaskDto;
import com.effectivemobile.practice3.model.entity.Task;
import com.effectivemobile.practice3.repository.impl.TaskRepositoryImpl;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.DisplayName.class)
class TaskControllerImplTestIT extends ConfigDB {

    private static final String API_PATH = "/v1/api";
    private static final String UPDATE = "/v1/api/update/";

    @Autowired
    private TaskRepositoryImpl taskRepository;
    @Autowired
    private TaskControllerImpl taskController;

    private Task task;
    private TaskDto taskDto1;
    private TaskDto taskDto;


    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(1L);
        task.setTitle("TEST TITLE");
        task.setDescription("TEST DESCR");

        taskDto1 = new TaskDto();
        taskDto1.setTitle("new");
        taskDto1.setDescription("new");

        taskDto = new TaskDto();
        taskDto.setDescription(task.getDescription());
        taskDto.setTitle(task.getTitle());
    }

    @AfterEach
    void tearDown() {
        task = null;
        taskDto = null;
        taskDto1 = null;
    }

    @Test
    @DisplayName(value = "1. Create task in db")
    void createTask_returnTask() {
        taskController.create(taskDto);
        assertEquals(0, taskRepository.findAll().collectList().map(List::size).block());
    }


    @Test
    @DisplayName(value = "2. Get list task")
    void getAllTask_ReturnAllTask() {
        taskRepository.deleteAll().block();
        Task task1 = Objects.requireNonNull(taskController.create(taskDto).getBody()).block();
        Task task2 = Objects.requireNonNull(taskController.create(taskDto1).getBody()).block();
        assertEquals(2, taskRepository.findAll().collectList().map(List::size).block());
    }

    @Test
    @DisplayName(value = "3. Update task in db")
    void updateById_ReturnTask() {
        taskRepository.deleteAll().block();
        Task task = Objects.requireNonNull(taskController.create(taskDto).getBody()).block();
        assertNotNull(task);
        Task taskNew =
                Objects.requireNonNull(taskController.update(task.getId(),
                        TaskDto.builder().title("new").description("new").build()).getBody()).block();
        assertNotNull(taskNew);
        assertEquals("new", taskNew.getDescription());
        assertEquals("new", taskNew.getTitle());
    }

    @Test
    @DisplayName(value = "4. Delete task in db")
    void deleteTaskById_ReturnTask() {
        taskRepository.deleteAll().block();
        Task task = Objects.requireNonNull(taskController.create(taskDto).getBody()).block();
        assertNotNull(task);
        taskController.deleteTaskById(task.getId()).block();
        Task find = taskRepository.findById(task.getId()).block();
        assertNull(find);
    }
}