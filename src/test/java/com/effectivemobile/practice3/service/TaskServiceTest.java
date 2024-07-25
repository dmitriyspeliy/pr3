package com.effectivemobile.practice3.service;

import com.effectivemobile.practice3.mapper.TaskMapper;
import com.effectivemobile.practice3.model.dto.TaskDto;
import com.effectivemobile.practice3.model.entity.Task;
import com.effectivemobile.practice3.repository.TaskRepositoryImpl;
import com.effectivemobile.practice3.utils.exception.BadRequestException;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
@ActiveProfiles(value = {"test"})
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
        when(taskRepository.findAll()).thenReturn(List.of(task));
        when(taskMapper.taskDtoList(List.of(task))).thenReturn(List.of(taskDto));

        //when
        List<TaskDto> allTask = taskService.getAllTask();

        //then
        assertEquals(1, allTask.size());
    }

    @Test
    @DisplayName(value = "2. Positive test. Method findByTaskId()")
    void findById_returnTask() throws BadRequestException {
        //mock
        when(taskRepository.findById(1L)).thenReturn(Optional.ofNullable(task));
        when(taskMapper.toDtoTask(task)).thenReturn(taskDto);

        //when
        TaskDto taskDtos = taskService.findByTaskId(task.getId());

        //then
        assertEquals(taskDtos.getTitle(), taskDto.getTitle());
    }

    @Test
    @DisplayName(value = "3. Negative test. Method findByTaskId()")
    void findById_taskNotExistInDb_return_exception() {
        //mock
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(BadRequestException.class, () -> {
            taskService.findByTaskId(task.getId());
        });

        String expectedMessage = "Couldn't find task by id";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName(value = "4. Positive test. Method refreshById()")
    void refreshTaskById_returnTask() throws BadRequestException {
        //mock
        when(taskRepository.findById(1L)).thenReturn(Optional.ofNullable(task));
        when(taskMapper.toDtoTask(task)).thenReturn(taskDto);
        when(taskMapper.toEntityTask(taskDto)).thenReturn(task);
        when(taskRepository.save(task)).thenReturn(Optional.ofNullable(task));

        //when
        TaskDto task1 = taskService.refreshById(1L, taskDto);

        //then
        assertEquals(taskDto.getTitle(), task1.getTitle());

    }

    @Test
    @DisplayName(value = "5. Negative test. Method refreshById()")
    void refreshTaskById_taskNotExistInDb_returnException() {
        //mock
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(BadRequestException.class, () -> {
            taskService.refreshById(task.getId(), taskDto);
        });

        String expectedMessage = "Couldn't find task by id";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName(value = "6. Positive test. Method deleteTaskById()")
    void deleteTaskById_nothingReturn() throws BadRequestException {
        //mock
        when(taskRepository.findById(1L)).thenReturn(Optional.ofNullable(task));
        when(taskMapper.toDtoTask(task)).thenReturn(taskDto);

        //when
        taskService.deleteTask(task.getId());

        verify(taskRepository, times(1)).deleteById(task.getId());
    }

    @Test
    @DisplayName(value = "7. Negative test. Method deleteTaskById()")
    void deleteTaskById_TaskNotExistById_returnException() {
        //mock
        when(taskRepository.findById(1L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(BadRequestException.class, () -> {
            taskService.deleteTask(task.getId());
        });

        String expectedMessage = "Couldn't find task by id";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }


    @Test
    @DisplayName(value = "8. Positive test. Method findByTitle()")
    void findByTitle_returnTask() throws BadRequestException {
        //mock
        when(taskRepository.findByTitle(anyString())).thenReturn(Optional.ofNullable(task));
        when(taskMapper.toDtoTask(task)).thenReturn(taskDto);

        //when
        TaskDto taskDtos = taskService.findByTitle(task.getTitle());

        //then
        assertEquals(taskDtos.getTitle(), task.getTitle());
    }

    @Test
    @DisplayName(value = "9. Positive test. Method createTask()")
    void createTask_returnTask() throws BadRequestException {
        //mock
        when(taskRepository.findByTitle(anyString())).thenReturn(Optional.empty());
        when(taskMapper.toEntityTask(any(TaskDto.class))).thenReturn(task);
        when(taskMapper.toDtoTask(any(Task.class))).thenReturn(taskDto);
        when(taskRepository.save(any(Task.class))).thenReturn(Optional.ofNullable(task));

        //when
        TaskDto taskDto1 = taskService.createTask(taskDto);

        //then
        assertEquals(taskDto1.getTitle(), taskDto.getTitle());
    }

    @Test
    @DisplayName(value = "9.1 Negative test. Method createTask()")
    void createTask_existByTitleInDB_returnException() {
        //mock
        when(taskRepository.findByTitle(anyString())).thenReturn(Optional.ofNullable(task));
        when(taskMapper.toDtoTask(task)).thenReturn(taskDto);

        Exception exception = assertThrows(BadRequestException.class, () -> {
            taskService.createTask(taskDto);
        });

        String expectedMessage = "Task exist in db";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}