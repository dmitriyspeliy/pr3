package com.effectivemobile.practice3.utils.exception;

import lombok.Getter;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
public class BadRequestException extends Exception {

    private final String textException;
    private final String code;

    public BadRequestException(String textException) {
        this.textException = textException;
        this.code = String.valueOf(BAD_REQUEST.value());
    }

    public BadRequestException(String textException, String code) {
        this.textException = textException;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return "\nERROR MESSAGE : " + textException + "\n" +
                "ERROR CODE : " + code;
    }

}