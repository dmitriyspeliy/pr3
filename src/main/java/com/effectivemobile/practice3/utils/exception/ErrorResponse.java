package com.effectivemobile.practice3.utils.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;


@Schema(description = "Объект ошибки")
public class ErrorResponse {
    @Schema(description = "Сообщение об ошибке")
    @JsonProperty("message")
    private String message;
    @Schema(description = "Код ошибки")
    @JsonProperty("code")
    private String code;

    public ErrorResponse(String message, String code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}