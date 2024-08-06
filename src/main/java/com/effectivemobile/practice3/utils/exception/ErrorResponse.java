package com.effectivemobile.practice3.utils.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Объект ошибки")
public class ErrorResponse {
    @Schema(description = "Сообщение об ошибке")
    @JsonProperty("message")
    private String message;
    @Schema(description = "Код ошибки")
    @JsonProperty("code")
    private String code;
}