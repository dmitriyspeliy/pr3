package com.effectivemobile.practice3.controller.impl;

import com.effectivemobile.practice3.model.dto.TaskDto;
import com.effectivemobile.practice3.model.entity.Task;
import com.effectivemobile.practice3.service.TaskService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@WebMvcTest(controllers = TaskControllerImpl.class)
@ActiveProfiles(value = {"test"})
class TaskControllerImplTest {

    private static final String API_PATH = "/v1/api";
    private static final String UPDATE = "/v1/api/update/";

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private Environment environment;
    @MockBean
    private TaskService taskService;

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
        taskDto1.setTitle(null);
        taskDto1.setDescription("");

        taskDto = new TaskDto();
        taskDto.setDescription(task.getDescription());
        taskDto.setTitle(task.getTitle());
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName(value = "0. Check test profile")
    void check_profile() {
        assertEquals("test", environment.getActiveProfiles()[0]);
    }

    @Test
    @DisplayName(value = "1. Positive test. Method create()")
    void createTask_returnTask() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post(API_PATH)
                        .content(new ObjectMapper().writeValueAsString(taskDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName(value = "2. Negative test. Method create()")
    void createTask_WithInvalidField_return400() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post(API_PATH)
                        .content(new ObjectMapper().writeValueAsString(taskDto1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }


    @Test
    @DisplayName(value = "3. Positive test. Method getAllTask()")
    void getAllTask_ReturnAllTask() throws Exception {
        Mockito.when(taskService.getAllTask()).thenReturn(List.of(taskDto));

        mockMvc.perform(MockMvcRequestBuilders
                        .get(API_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName(value = "4. Positive test. Method update()")
    void updateById_ReturnTask() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post(UPDATE + 1)
                        .content(new ObjectMapper().writeValueAsString(taskDto))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    @DisplayName(value = "5. Negative test. Method update()")
    void updateById_WithInvalidParam_ReturnException() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .post(UPDATE + 1)
                        .content(new ObjectMapper().writeValueAsString(taskDto1))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    @DisplayName(value = "6. Positive test. Method deleteTaskById()")
    void deleteTaskById_ReturnTask() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders
                        .get(API_PATH + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());
    }
}