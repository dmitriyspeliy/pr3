package com.effectivemobile.practice3.utils.exception;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Объект ошибки")
public class ErrorResponse {
    @Schema(description = "Сообщение об ошибке")
    @JsonProperty("message")
    String message;
    @Schema(description = "Код ошибки")
    @JsonProperty("code")
    String code;
}