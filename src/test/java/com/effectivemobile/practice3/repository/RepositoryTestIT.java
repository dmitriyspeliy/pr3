package com.effectivemobile.practice3.repository;

import com.effectivemobile.practice3.configDB_IT.ConfigDB;
import com.effectivemobile.practice3.model.entity.Task;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


public class RepositoryTestIT extends ConfigDB {

    @Autowired
    private TaskRepository taskRepository;

    @DisplayName(value = "Smoke test")
    @Test
    void saveInDB_returnTask() {
        assertNotNull(taskRepository);
        taskRepository.deleteAll();
        Task taskNew = taskRepository.save(Task.builder().title("test").description("test").build());
        assertEquals(1L, taskNew.getId());
    }


}
