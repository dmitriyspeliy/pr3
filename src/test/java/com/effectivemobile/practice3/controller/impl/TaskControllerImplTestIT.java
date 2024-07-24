package com.effectivemobile.practice3.controller.impl;

import com.effectivemobile.practice3.configDB_IT.ConfigDB;
import com.effectivemobile.practice3.model.dto.TaskDto;
import com.effectivemobile.practice3.model.entity.Task;
import com.effectivemobile.practice3.repository.TaskRepository;
import com.effectivemobile.practice3.utils.exception.BadRequestException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.DisplayName.class)
class TaskControllerImplTestIT extends ConfigDB {

    private static final String API_PATH = "/v1/api";
    private static final String UPDATE = "/v1/api/update/";

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private TaskControllerImpl taskController;

    private Task task;
    private TaskDto taskDto1;
    private TaskDto taskDto;


    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(1L);
        task.setTitle("new");
        task.setDescription("new");

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
    void createTask_returnTask() throws BadRequestException {
        taskRepository.deleteAll();
        taskController.create(taskDto);
        assertEquals(1, taskRepository.findAll().size());
    }


    @Test
    @DisplayName(value = "2. Get list task")
    void getAllTask_ReturnAllTask() throws BadRequestException {
        taskRepository.deleteAll();
        TaskDto task1 = taskController.create(taskDto).getBody();
        assertEquals(1, taskRepository.findAll().size());
    }

    @Test
    @DisplayName(value = "3. Update task in db")
    void updateById_ReturnTask() throws BadRequestException {
        taskRepository.deleteAll();
        Task task1 = taskRepository.save(task);
        assertNotNull(task1);
        TaskDto taskNew =
                taskController.update(task1.getId(),
                        TaskDto.builder().title("new1").description("new1").build()).getBody();
        assertNotNull(taskNew);
        assertEquals("new1", taskNew.getDescription());
        assertEquals("new1", taskNew.getTitle());
    }

    @Test
    @DisplayName(value = "4. Delete task in db")
    void deleteTaskById_ReturnTask() throws BadRequestException {
        taskRepository.deleteAll();
        Task task1 = taskRepository.save(task);
        assertNotNull(task1);
        taskController.deleteTaskById(task1.getId());
        Optional<Task> find = taskRepository.findById(task.getId());
        assertTrue(find.isEmpty());
    }
}