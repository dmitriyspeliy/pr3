package com.effectivemobile.practice3.controller;

import com.effectivemobile.practice3.model.dto.TaskDto;
import com.effectivemobile.practice3.model.entity.Task;
import com.effectivemobile.practice3.utils.exception.BadRequestException;
import com.effectivemobile.practice3.utils.exception.ErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Tag(name = "TODO controller", description = "Controller with simple rest methods")
@Validated
public interface TaskController {

    @Operation(
            summary = "Create new task",
            responses = {
                    @ApiResponse(
                            description = "New task",
                            responseCode = "200",
                            content = {@Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Task.class)))}
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "This task already exist by title",
                            content = {@Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))}),
                    @ApiResponse(
                            responseCode = "5XX",
                            description = "See reason in error message",
                            content = {@Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))})
            }
    )
    ResponseEntity<TaskDto> create(@Parameter(description = "Body task") @Valid TaskDto taskDto) throws BadRequestException;

    @Operation(
            summary = "Get all task",
            responses = {
                    @ApiResponse(
                            description = "Get array with task",
                            responseCode = "200",
                            content = {@Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Task.class)))}
                    ),
                    @ApiResponse(
                            responseCode = "5XX",
                            description = "See reason in error message",
                            content = {@Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))})
            }
    )
    List<TaskDto> getAllTask();

    @Operation(
            summary = "Refresh task by id",
            responses = {
                    @ApiResponse(
                            description = "New task",
                            responseCode = "200",
                            content = {@Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = Task.class)))}
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Couldn't find task by id",
                            content = {@Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))}),
                    @ApiResponse(
                            responseCode = "5XX",
                            description = "See reason in error message",
                            content = {@Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))})
            }
    )
    ResponseEntity<TaskDto> update(@Parameter(description = "Task's id") @Min(1) Long id,
                                @Parameter(description = "Body task") @Valid TaskDto taskDto) throws BadRequestException;

    @Operation(
            summary = "Delete task by id",
            responses = {
                    @ApiResponse(
                            description = "Task was deleted",
                            responseCode = "200",
                            content = {@Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema()))}
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Couldn't find task by id",
                            content = {@Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))}),
                    @ApiResponse(
                            responseCode = "5XX",
                            description = "See reason in error message",
                            content = {@Content(mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = ErrorResponse.class)))})
            }
    )
    void deleteTaskById(@Parameter(description = "Task's id") @Min(1) Long id) throws BadRequestException;
}
