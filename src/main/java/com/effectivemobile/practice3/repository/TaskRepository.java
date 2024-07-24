package com.effectivemobile.practice3.repository;

import com.effectivemobile.practice3.model.entity.Task;
import com.effectivemobile.practice3.utils.exception.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TaskRepository implements TaskJdbcTemplate<Task> {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Optional<Task> findByTitle(String title) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("SELECT * FROM task WHERE title=?",
                    BeanPropertyRowMapper.newInstance(Task.class), title));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Task> save(Task task) throws BadRequestException {
        try {
            jdbcTemplate.update("INSERT INTO task (title, description) VALUES (?,?)", task.getTitle(), task.getDescription());
        } catch (DataAccessException e) {
            throw new BadRequestException(e.getMessage());
        }
        return findByTitle(task.getTitle());
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update("DELETE FROM task WHERE id=?", id);
    }

    @Override
    public void deleteAll() {
        jdbcTemplate.update("DELETE from task");
    }

    @Override
    public Optional<Task> findById(Long id) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("select * from task where id = ?",
                    (resultSet, rowNum) -> {
                        Task task = new Task();
                        task.setTitle(resultSet.getString("title"));
                        task.setDescription(resultSet.getString("description"));
                        return task;
                    }, id));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }


    @Override
    public List<Task> findAll() {
        return jdbcTemplate.query("SELECT * from task", BeanPropertyRowMapper.newInstance(Task.class));
    }

}
