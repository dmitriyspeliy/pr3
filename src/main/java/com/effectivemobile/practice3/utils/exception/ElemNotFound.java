package com.effectivemobile.practice3.utils.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class ElemNotFound extends Exception {

    private final String textException;
    private final String code;

    public String getTextException() {
        return textException;
    }

    public String getCode() {
        return code;
    }

    public ElemNotFound(String textException) {
        this.textException = textException;
        this.code = String.valueOf(BAD_REQUEST.value());
    }

    public ElemNotFound(String textException, String code) {
        this.textException = textException;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return "\nERROR MESSAGE : " + textException + "\n" +
                "ERROR CODE : " + code;
    }
}