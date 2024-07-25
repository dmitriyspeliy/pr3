package com.effectivemobile.practice3.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


@Schema(description = "Task")
public class TaskDto {

    @Schema(description = "Task title",
            example = "Fix bug")
    @JsonProperty(value = "title")
    @Size(max = 100)
    @NotNull(message = "Title mustn't null")
    @NotEmpty(message = "Title must be filled in")
    String title;

    @Schema(description = "Description",
            example = "Task description")
    @JsonProperty(value = "description")
    @Size(max = 1000)
    @NotNull(message = "Description mustn't null")
    @NotEmpty(message = "Description must be filled in")
    String description;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
