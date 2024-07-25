package com.effectivemobile.practice3.service;

import com.effectivemobile.practice3.mapper.TaskMapper;
import com.effectivemobile.practice3.model.dto.TaskDto;
import com.effectivemobile.practice3.model.entity.Task;
import com.effectivemobile.practice3.repository.TaskRepositoryImpl;
import com.effectivemobile.practice3.utils.exception.BadRequestException;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class TaskService {

    private final TaskRepositoryImpl taskRepository;
    private final TaskMapper taskMapper;
    private final String cleanRate = "600000"; //10 mins
    private final Logger log = Logger.getLogger(TaskService.class.getName());


    public TaskService(TaskRepositoryImpl taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }


    @Cacheable("tasks")
    public List<TaskDto> getAllTask() {
        return taskMapper.taskDtoList(taskRepository.findAll());
    }

    @CacheEvict(value = "tasks", allEntries = true)
    @Scheduled(fixedRateString = cleanRate)
    public void emptyTasksCache() {
        log.info("Emptying Tasks cache");
    }

    @Cacheable("task")
    public TaskDto findByTaskId(Long taskId) throws BadRequestException {
        log.info("Find by task id " + taskId);
        Optional<Task> taskOptional = taskRepository.findById(taskId);
        if (taskOptional.isPresent()) {
            return taskMapper.toDtoTask(taskOptional.get());
        } else {
            throw new BadRequestException("Couldn't find task by id " + taskId);
        }
    }

    @CacheEvict(value = "task", allEntries = true)
    @Scheduled(fixedRateString = cleanRate)
    public void emptyTaskCache() {
        log.info("Emptying Task cache");
    }

    public void deleteTask(Long taskId) throws BadRequestException {
        log.info("Delete by task id " + taskId);
        findByTaskId(taskId);
        taskRepository.deleteById(taskId);
    }

    public TaskDto refreshById(Long taskId, TaskDto taskDto) throws BadRequestException {
        log.info("Refresh task by id " + taskId);
        TaskDto newTask = findByTaskId(taskId);
        newTask.setTitle(taskDto.getTitle());
        newTask.setDescription(taskDto.getDescription());
        taskRepository.save(taskMapper.toEntityTask(taskDto));
        return taskDto;
    }

    public TaskDto findByTitle(String title) throws BadRequestException {
        log.info("Find task by title " + title);
        Optional<Task> taskOptional = taskRepository.findByTitle(title);
        if (taskOptional.isPresent()) {
            return taskMapper.toDtoTask(taskOptional.get());
        } else {
            throw new BadRequestException("Couldn't find task by title " + title);
        }
    }

    public TaskDto createTask(TaskDto taskDto) throws BadRequestException {
        log.info("Create new task");
        TaskDto newTask;
        try {
            findByTitle(taskDto.getTitle());
        } catch (BadRequestException e) {
            newTask = new TaskDto();
            newTask.setDescription(taskDto.getDescription());
            newTask.setTitle(taskDto.getTitle());
            return taskMapper.toDtoTask(taskRepository.save(taskMapper.toEntityTask(newTask)).get());
        }
        throw new BadRequestException("Task exist in db");
    }

}
