package com.effectivemobile.practice3.mapper;

import com.effectivemobile.practice3.model.dto.TaskDto;
import com.effectivemobile.practice3.model.entity.Task;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskMapper {

    public Task toEntityTask(TaskDto taskDto) {
        return Task.builder()
                .title(taskDto.getTitle())
                .description(taskDto.getDescription())
                .build();
    }

    public TaskDto toDtoTask(Task task) {
        return TaskDto.builder()
                .title(task.getTitle())
                .description(task.getDescription())
                .build();
    }

    public List<TaskDto> taskDtoList(List<Task> taskList) {
        return taskList.stream().map(this::toDtoTask).collect(Collectors.toList());
    }


}
