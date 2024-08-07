package com.effectivemobile.practice3.repository.impl;

import com.effectivemobile.practice3.model.dto.TaskDto;
import com.effectivemobile.practice3.model.entity.Task;
import com.effectivemobile.practice3.repository.TaskRepository;
import io.r2dbc.spi.Row;
import io.r2dbc.spi.RowMetadata;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;

@Service
@RequiredArgsConstructor
public class TaskRepositoryImpl implements TaskRepository<Task> {

    private final DatabaseClient databaseClient;

    @Override
    public Mono<Task> findByTitle(String title) {
        return this.databaseClient
                .sql("SELECT * FROM task WHERE title=:title")
                .bind("title", title)
                .map(MAPPING_FUNCTION)
                .one();

    }

    @Override
    public Mono<Task> save(Task task) {
        return this.databaseClient.sql("INSERT INTO  task (title, description) VALUES (:title, :description)")
                .filter((statement, executeFunction) -> statement.returnGeneratedValues("id", "title", "description").execute())
                .bind("title", task.getTitle())
                .bind("description", task.getDescription())
                .map(MAPPING_FUNCTION)
                .one();
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return this.databaseClient
                .sql("DELETE FROM task WHERE id=:id")
                .bind("id", id)
                .then();
    }

    @Override
    public Mono<Task> findById(Long id) {
        return this.databaseClient
                .sql("SELECT * FROM task WHERE id=:id")
                .bind("id", id)
                .map(MAPPING_FUNCTION)
                .one();
    }

    @Override
    public Flux<Task> findAll(Integer limit, Integer offset) {
        return this.databaseClient
                .sql("SELECT * FROM task limit " + limit + " offset " + offset)
                .map(MAPPING_FUNCTION)
                .all();
    }

    public Mono<Void> deleteAll() {
        return this.databaseClient
                .sql("DELETE FROM task")
                .then();
    }

    @Override
    public Mono<Long> updateById(Long id, TaskDto taskDto) {
        return this.databaseClient.sql("UPDATE task set title=:title, description=:description WHERE id=:id")
                .bind("title", taskDto.getTitle())
                .bind("description", taskDto.getDescription())
                .bind("id", id)
                .fetch().rowsUpdated();
    }

    public static final BiFunction<Row, RowMetadata, Task> MAPPING_FUNCTION = (row, rowMetaData) -> Task.builder()
            .id(row.get("id", Long.class))
            .title(row.get("title", String.class))
            .description(row.get("description", String.class))
            .build();
}
