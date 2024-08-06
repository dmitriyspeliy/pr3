package com.effectivemobile.practice3.controller.impl;

import com.effectivemobile.practice3.model.dto.TaskDto;
import com.effectivemobile.practice3.model.entity.Task;
import com.effectivemobile.practice3.service.TaskService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestMethodOrder(MethodOrderer.DisplayName.class)
@WebFluxTest(controllers = TaskControllerImpl.class)
@ActiveProfiles(value = {"test"})
class TaskControllerImplTest {

    private static final String API_PATH = "/v1/api";
    private static final String UPDATE = "/v1/api/update/";

    @Autowired
    private WebTestClient webTestClient;
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
        taskDto1.setDescription(null);

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
    void createTask_returnTask() {

        Mockito.when(taskService.createTask(taskDto)).thenReturn(Mono.just(task));

        webTestClient.post()
                .uri(API_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(taskDto), TaskDto.class)
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    @DisplayName(value = "2. Negative test. Method create()")
    void createTask_WithInvalidField_return400() {

        Mockito.when(taskService.createTask(taskDto)).thenReturn(Mono.just(task));

        webTestClient.post()
                .uri(API_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(taskDto1), TaskDto.class)
                .exchange()
                .expectStatus().is4xxClientError();
    }


    @Test
    @DisplayName(value = "3. Positive test. Method getAllTask()")
    void getAllTask_ReturnAllTask() {
        Mockito.when(taskService.getAllTask(10, 1)).thenReturn(Flux.just(task));

        webTestClient.get()
                .uri(API_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    @DisplayName(value = "4. Positive test. Method update()")
    void updateById_ReturnTask() {

        Mockito.when(taskService.refreshById(1L, taskDto)).thenReturn(Mono.just(task));

        webTestClient.put()
                .uri(UPDATE + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(taskDto), TaskDto.class)
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    @DisplayName(value = "5. Negative test. Method update()")
    void updateById_WithInvalidParam_ReturnException() {

        Mockito.when(taskService.refreshById(1L, taskDto)).thenReturn(Mono.just(task));

        webTestClient.post()
                .uri(UPDATE + 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(taskDto1), TaskDto.class)
                .exchange()
                .expectStatus().is4xxClientError();
    }

    @Test
    @DisplayName(value = "6. Positive test. Method deleteTaskById()")
    void deleteTaskById_ReturnTask() {

        Mockito.when(taskService.deleteTask(1L)).thenReturn(Mono.empty());

        webTestClient.delete()
                .uri(API_PATH + "/1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().is2xxSuccessful();

    }
}