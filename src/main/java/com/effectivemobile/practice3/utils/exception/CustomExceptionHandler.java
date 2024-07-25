package com.effectivemobile.practice3.utils.exception;


import jakarta.validation.ConstraintViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class CustomExceptionHandler {


    /**
     * 400
     */
    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<ErrorResponse> badRequestException(BadRequestException ex) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(new ErrorResponse(ex.getTextException(), ex.getCode()));
    }

    /**
     * 404
     */
    @ExceptionHandler(ElemNotFound.class)
    public final ResponseEntity<ErrorResponse> elemNotFound(ElemNotFound ex) {
        return ResponseEntity
                .status(NOT_FOUND)
                .body(new ErrorResponse(ex.getTextException(), ex.getCode()));
    }


    /**
     * 400
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class,
            ConstraintViolationException.class})
    public final ResponseEntity<ErrorResponse> invalidParam() {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(new ErrorResponse("Invalid param or method does not exist", String.valueOf(BAD_REQUEST.value())));
    }

}