package com.effectivemobile.practice3.repository;

import com.effectivemobile.practice3.utils.exception.BadRequestException;

import java.util.List;
import java.util.Optional;

public interface TaskJdbcTemplate<T> {

    Optional<T> findByTitle(String title);

    Optional<T> save(T task) throws BadRequestException;

    void deleteById(Long id);

    Optional<T> findById(Long id);

    List<T> findAll();

    void deleteAll();

}