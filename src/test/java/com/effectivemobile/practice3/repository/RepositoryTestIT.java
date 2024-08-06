package com.effectivemobile.practice3.repository;

import com.effectivemobile.practice3.configDB_IT.ConfigDB;
import com.effectivemobile.practice3.model.entity.Task;
import com.effectivemobile.practice3.repository.impl.TaskRepositoryImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import reactor.core.publisher.Mono;

import java.util.Objects;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class RepositoryTestIT extends ConfigDB {

    @Autowired
    private TaskRepositoryImpl taskRepository;

    @DisplayName(value = "Find by title")
    @Test
    @Sql("/sql/schema.sql")
    void saveInDB_returnTask() {
        assertNotNull(taskRepository);
        Mono<Task> test = taskRepository.findByTitle("test");
        assertEquals(1L, Objects.requireNonNull(test.block()).getId());
    }


}
