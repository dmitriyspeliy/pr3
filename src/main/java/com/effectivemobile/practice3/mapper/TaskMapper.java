package com.effectivemobile.practice3.mapper;

import com.effectivemobile.practice3.model.dto.TaskDto;
import com.effectivemobile.practice3.model.entity.Task;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskMapper {

    public Task toEntityTask(TaskDto taskDto) {
        Task task = new Task();
        task.setTitle(taskDto.getTitle());
        task.setDescription(taskDto.getDescription());
        return task;
    }

    public TaskDto toDtoTask(Task task) {
        TaskDto taskDto = new TaskDto();
        taskDto.setTitle(task.getTitle());
        taskDto.setDescription(task.getDescription());
        return taskDto;
    }

    public List<TaskDto> taskDtoList(List<Task> taskList) {
        return taskList.stream().map(this::toDtoTask).collect(Collectors.toList());
    }


}
