package com.effectivemobile.practice3.service;

import com.effectivemobile.practice3.mapper.TaskMapper;
import com.effectivemobile.practice3.model.dto.TaskDto;
import com.effectivemobile.practice3.model.entity.Task;
import com.effectivemobile.practice3.repository.impl.TaskRepositoryImpl;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
class TaskServiceTest {

    @Mock
    TaskRepositoryImpl taskRepository;
    @Mock
    TaskMapper taskMapper;

    @InjectMocks
    TaskService taskService;

    private Task task;
    private TaskDto taskDto;

    @BeforeEach
    void setUp() {
        task = new Task();
        task.setId(1L);
        task.setTitle("TEST TITLE");
        task.setDescription("TEST DESCR");

        taskDto = new TaskDto();
        taskDto.setDescription(task.getDescription());
        taskDto.setTitle(task.getTitle());
    }

    @AfterEach
    void tearDown() {
        task = null;
        taskDto = null;
    }

    @Test
    @DisplayName(value = "1. Positive test. Method getAllTask()")
    void getAllTask_returnAllTask() {
        //mock
        when(taskRepository.findAll()).thenReturn(Flux.just(task));

        //when
        StepVerifier.create(taskService.getAllTask())
                //then
                .expectNext(task)
                .verifyComplete();
    }

    @Test
    @DisplayName(value = "2. Positive test. Method findByTaskId()")
    void findById_returnTask() {

        //mock
        when(taskRepository.findById(1L)).thenReturn(Mono.just(task));

        //when
        StepVerifier.create(taskService.findByTaskId(1L))
                //then
                .expectNext(task)
                .verifyComplete();
    }

    @Test
    @DisplayName(value = "3. Positive test. Method refreshById()")
    void refreshTaskById_returnTask() {
        //mock
        when(taskRepository.findById(1L)).thenReturn(Mono.just(task));
        when(taskRepository.updateById(1L, taskDto)).thenReturn(Mono.just(task));

        //when
        Mono<Task> taskMono = taskService.refreshById(1L, taskDto);

        //when
        StepVerifier.create(taskMono)
                //then
                .expectNext(task)
                .verifyComplete();

    }

    @Test
    @DisplayName(value = "4. Negative test. Method refreshById()")
    void refreshTaskById_taskNotExistInDb_returnException() {
        //mock
        when(taskRepository.findById(1L)).thenReturn(Mono.empty());

        //when
        Mono<Task> taskMono = taskService.refreshById(1L, taskDto);

        StepVerifier.create(taskMono)
                //then
                .expectError().verify();
    }

    @Test
    @DisplayName(value = "5. Positive test. Method deleteTaskById()")
    void deleteTaskById_returnTask() {
        //mock
        when(taskRepository.findById(1L)).thenReturn(Mono.just(task));
        when(taskRepository.deleteById(1L)).thenReturn(Mono.empty());

        //when
        StepVerifier.create(taskService.deleteTask(1L))
                //then
                .verifyComplete();
    }

    @Test
    @DisplayName(value = "6. Negative test. Method deleteTaskById()")
    void deleteTaskById_TaskNotExistById_returnException() {
        //mock
        when(taskRepository.findById(1L)).thenReturn(Mono.empty());

        StepVerifier.create(taskService.deleteTask(1L))
                //then
                .expectError().verify();
    }


    @Test
    @DisplayName(value = "7. Positive test. Method findByTitle()")
    void findByTitle_returnTask() {
        //mock
        when(taskRepository.findByTitle(task.getTitle())).thenReturn(Mono.just(task));

        //when
        StepVerifier.create(taskService.findByTitle(task.getTitle()))
                //then
                .expectNext(task)
                .verifyComplete();

    }

    @Test
    @DisplayName(value = "8. Positive test. Method createTask()")
    void createTask_returnTask() {
        //mock
        when(taskRepository.findByTitle(task.getTitle())).thenReturn(Mono.empty());
        when(taskRepository.save(task)).thenReturn(Mono.just(task));
        when(taskMapper.toEntityTask(taskDto)).thenReturn(task);

        //when
        Mono<Task> taskMono = taskService.createTask(taskDto);

        //when
        StepVerifier.create(taskMono)
                //then
                .expectNext(task)
                .verifyComplete();
    }

    @Test
    @DisplayName(value = "9. Negative test. Method createTask()")
    void createTask_existByTitleInDB_returnException() {
        when(taskRepository.findByTitle(task.getTitle())).thenReturn(Mono.just(task));

        //when
        Mono<Task> taskMono = taskService.createTask(taskDto);

        StepVerifier.create(taskMono)
                //then
                .expectError().verify();
    }
}