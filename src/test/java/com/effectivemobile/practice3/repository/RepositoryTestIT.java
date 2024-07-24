package com.effectivemobile.practice3.repository;

import com.effectivemobile.practice3.configDB_IT.ConfigDB;
import com.effectivemobile.practice3.model.entity.Task;
import com.effectivemobile.practice3.repository.impl.TaskRepositoryImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class RepositoryTestIT extends ConfigDB {

    @Autowired
    private TaskRepositoryImpl taskRepository;

    @DisplayName(value = "Smoke test")
    @Test
    void saveInDB_returnTask() {
        assertNotNull(taskRepository);
        taskRepository.deleteAll().block();
        Task taskNew = taskRepository.save(Task.builder().title("test").description("test").build()).block();
        assertEquals(1L, taskNew.getId());
    }


}
