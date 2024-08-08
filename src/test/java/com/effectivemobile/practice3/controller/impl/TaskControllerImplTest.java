package com.effectivemobile.practice3.controller.impl;

import com.effectivemobile.practice3.model.dto.TaskDto;
import com.effectivemobile.practice3.model.entity.Task;
import com.effectivemobile.practice3.service.TaskService;
import com.effectivemobile.practice3.utils.exception.ErrorResponse;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@WebFluxTest(controllers = TaskControllerImpl.class)
@ActiveProfiles(value = {"test"})
class TaskControllerImplTest {

    private static final String API_PATH = "/api/v1";

    @Autowired
    private WebTestClient webTestClient;
    @MockBean
    private TaskService taskService;

    private Task task;
    private TaskDto taskDtoWithNullTitle;
    private TaskDto validTaskDto;


    @BeforeEach
    void init() {
        task = new Task();
        task.setId(1L);
        task.setTitle("TEST TITLE");
        task.setDescription("TEST DESCR");

        validTaskDto = new TaskDto();
        validTaskDto.setDescription(task.getDescription());
        validTaskDto.setTitle(task.getTitle());

        taskDtoWithNullTitle = new TaskDto();
        taskDtoWithNullTitle.setTitle(null);
        taskDtoWithNullTitle.setDescription("description");
    }

    @AfterEach
    void clear() {
        task = null;
        validTaskDto = null;
        taskDtoWithNullTitle = null;
    }

    @Test
    @DisplayName(value = "Create task with valid param. Return task")
    @Order(1)
    void given_ValidTask_when_createTask_returnTask() {
        //Mock
        Mockito.when(taskService.createTask(any(TaskDto.class))).thenReturn(Mono.just(task));

        //When
        webTestClient.post()
                .uri(API_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(validTaskDto), TaskDto.class)
                .exchange()
                //Then
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.title").isEqualTo(validTaskDto.getTitle())
                .jsonPath("$.description").isEqualTo(validTaskDto.getDescription());
    }

    @Test
    @DisplayName(value = "Create task with title null. Return exception")
    @Order(1)
    void given_FieldTitleEqualsNull_when_createTask_return400() {
        //When
        webTestClient.post()
                .uri(API_PATH)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(taskDtoWithNullTitle), TaskDto.class)
                .exchange()
                //Then
                .expectStatus().is4xxClientError()
                .expectBody(ErrorResponse.class)
                .value((errorResponse) -> {
                    String code = errorResponse.getCode();
                    String message = errorResponse.getMessage();
                    assertTrue(message.contains("Title mustn't null"));
                    assertEquals("400", code);
                });
    }

    @Test
    @DisplayName(value = "Get all task. Return task list")
    @Order(2)
    void given_validParam_whenGetAllList_returnListTask() {
        // Mock
        Mockito.when(taskService.getAllTask(anyInt(), anyInt())).thenReturn(Flux.just(task));

        // when
        webTestClient.get()
                .uri(API_PATH + "?limit=1&numberOfPage=1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                // Then
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$").isArray()
                .jsonPath("$[0].id").isEqualTo("1")
                .jsonPath("$[0].title").isEqualTo("TEST TITLE")
                .jsonPath("$[0].description").isEqualTo("TEST DESCR");
    }

    @Test
    @DisplayName(value = "Get all task with limit less 1. Return exception")
    @Order(2)
    void given_InvalidLimit_whenGetAllList_returnException() {
        // When
        webTestClient.get()
                .uri(API_PATH + "?limit=0&numberOfPage=1")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                // Then
                .expectStatus().is4xxClientError()
                .expectBody(ErrorResponse.class)
                .value((errorResponse) -> {
                    String code = errorResponse.getCode();
                    String message = errorResponse.getMessage();
                    assertTrue(message.contains("getAllTask.limit: must be greater than or equal to 1"));
                    assertEquals("400", code);
                });
    }

    @Test
    @DisplayName(value = "Get all task with number of page less 1. Return exception")
    @Order(2)
    void given_InvalidNumberOfPage_whenGetAllList_returnException() {
        // When
        webTestClient.get()
                .uri(API_PATH + "?limit=1&numberOfPage=0")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                // Then
                .expectStatus().is4xxClientError()
                .expectBody(ErrorResponse.class)
                .value((errorResponse) -> {
                    String code = errorResponse.getCode();
                    String message = errorResponse.getMessage();
                    assertTrue(message.contains("getAllTask.numberOfPage: must be greater than or equal to 1"));
                    assertEquals("400", code);
                });
    }

    @Test
    @DisplayName(value = "Update task by exist id. Return updated task")
    @Order(3)
    void given_existId_when_UpdateById_returnUpdatedTask() {
        // Mock
        Mockito.when(taskService.refreshById(anyLong(), any(TaskDto.class))).thenReturn(Mono.just(task));

        // When
        webTestClient.put()
                .uri(API_PATH + "/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(validTaskDto), TaskDto.class)
                // Then
                .exchange()
                .expectStatus().is2xxSuccessful()
                .expectBody()
                .jsonPath("$.title").isEqualTo(validTaskDto.getTitle())
                .jsonPath("$.description").isEqualTo(validTaskDto.getDescription());
    }

    @Test
    @DisplayName(value = "Update task by id less than 1. Return exception")
    @Order(3)
    void given_idLessThan1_when_UpdateById_returnException() {
        // When
        webTestClient.put()
                .uri(API_PATH + "/0")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(validTaskDto), TaskDto.class)
                // Then
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(ErrorResponse.class)
                .value((errorResponse) -> {
                    String code = errorResponse.getCode();
                    String message = errorResponse.getMessage();
                    assertTrue(message.contains("update.id: must be greater than or equal to 1"));
                    assertEquals("400", code);
                });
    }

    @Test
    @DisplayName(value = "Update task by id with invalid task dto. Return exception")
    @Order(3)
    void given_inValidTaskDto_when_UpdateById_returnException() {
        //When
        webTestClient.put()
                .uri(API_PATH + "/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(taskDtoWithNullTitle), TaskDto.class)
                .exchange()
                //Then
                .expectStatus().is4xxClientError()
                .expectBody(ErrorResponse.class)
                .value((errorResponse) -> {
                    String code = errorResponse.getCode();
                    String message = errorResponse.getMessage();
                    assertTrue(message.contains("Title mustn't null"));
                    assertEquals("400", code);
                });
    }

    @Test
    @DisplayName(value = "Delete task by id. Return ok")
    @Order(4)
    void given_existId_whenDeleteTask_returnOk() {
        // Mock
        Mockito.when(taskService.deleteTask(anyLong())).thenReturn(Mono.empty());

        // When
        webTestClient.delete()
                .uri(API_PATH + "/1")
                .accept(MediaType.APPLICATION_JSON)
                // Then
                .exchange()
                .expectStatus().is2xxSuccessful();
    }

    @Test
    @DisplayName(value = "Delete task by id less than 1. Return exception")
    @Order(4)
    void given_idLessOne_whenDeleteTask_returnException() {
        // Mock
        Mockito.when(taskService.deleteTask(anyLong())).thenReturn(Mono.empty());

        // When
        webTestClient.delete()
                .uri(API_PATH + "/0")
                .accept(MediaType.APPLICATION_JSON)
                // Then
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(ErrorResponse.class)
                .value((errorResponse) -> {
                    String code = errorResponse.getCode();
                    String message = errorResponse.getMessage();
                    assertTrue(message.contains("deleteTaskById.id: must be greater than or equal to 1"));
                    assertEquals("400", code);
                });
    }


}