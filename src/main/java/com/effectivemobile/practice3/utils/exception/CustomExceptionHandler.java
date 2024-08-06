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
                .body(ErrorResponse.builder()
                        .message(ex.getTextException())
                        .code(ex.getCode())
                        .build());
    }

    /**
     * 404
     */
    @ExceptionHandler(ElementNotFound.class)
    public final ResponseEntity<ErrorResponse> elemNotFound(ElementNotFound ex) {
        return ResponseEntity
                .status(NOT_FOUND)
                .body(ErrorResponse.builder()
                        .message(ex.getTextException())
                        .code(ex.getCode())
                        .build());
    }


    /**
     * 400
     */
    @ExceptionHandler(value = {MethodArgumentNotValidException.class, MethodArgumentTypeMismatchException.class,
            ConstraintViolationException.class})
    public final ResponseEntity<ErrorResponse> invalidParam() {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(ErrorResponse.builder()
                        .message("Invalid param or method does not exist")
                        .code(String.valueOf(BAD_REQUEST.value()))
                        .build());
    }

}