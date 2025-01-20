package ru.kostrykinmark.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kostrykinmark.comment.dto.CommentResponseDto;
import ru.kostrykinmark.comment.dto.NewCommentDto;
import ru.kostrykinmark.comment.service.CommentService;
import ru.kostrykinmark.task.dto.TaskFullDto;
import ru.kostrykinmark.task.dto.UpdateTaskUserRequest;
import ru.kostrykinmark.task.service.TaskService;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
@Log4j2
public class UserController {
    private static final String ADD_COMMENT_OPERATION_SUMMARY = "Добавление новых комментариев";
    private static final String ADD_COMMENT_METHOD_DESCRIPTION = "Добавляет новый комментарий, на вход принимает пользователя,задачу" +
            " и текст комментария.";

    private static final String UPDATE_TASK_OPERATION_SUMMARY = "Обновление задачи";
    private static final String UPDATE_TASK_METHOD_DESCRIPTION = "Обновляет пользовательскую задачу, на вход принимает пользователя,задачу" +
            " и статус задачи.";
    private static final String LOGGER_ADD_COMMENT_MESSAGE = "Adding comment to task with id {} : {} ";
    private static final String LOGGER_UPDATE_TASK_MESSAGE = "Updating task : {}";
    private final TaskService taskService;
    private final CommentService commentService;

    @Operation(summary = ADD_COMMENT_OPERATION_SUMMARY,
            description = ADD_COMMENT_METHOD_DESCRIPTION)
    @PostMapping("/{userId}/tasks/{taskId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public CommentResponseDto addComment(@Parameter(description = "Идентификатор пользователя")
                                         @PathVariable("userId") int userId,
                                         @Parameter(description = "Идентификатор задачи")
                                         @PathVariable("taskId") int taskId,
                                         @io.swagger.v3.oas.annotations.parameters.RequestBody(
                                                 description = "Комментарий для добавления", required = true,
                                                 content = @Content(mediaType = "application/json",
                                                         schema = @Schema(implementation = NewCommentDto.class),
                                                         examples = @ExampleObject(value = "{ \"text\": \"My comment\"}")))
                                         @RequestBody @Validated NewCommentDto commentDto) {
        log.info(LOGGER_ADD_COMMENT_MESSAGE, taskId, commentDto);
        return commentService.add(userId, taskId, commentDto);
    }

    @Operation(summary = UPDATE_TASK_OPERATION_SUMMARY,
            description = UPDATE_TASK_METHOD_DESCRIPTION)
    @PatchMapping("/{userId}/tasks/{taskId}")
    public TaskFullDto updateUserTask(
            @Parameter(description = "Идентификатор пользователя")
            @PathVariable("userId") int userId,
            @Parameter(description = "Идентификатор задачи")
            @PathVariable("taskId") int taskId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Статус задачи для обновления", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UpdateTaskUserRequest.class),
                            examples = @ExampleObject(value = "{ \"status\": \"FINISHED\"}")))
            @RequestBody @Validated UpdateTaskUserRequest userEventRequest) {
        log.info(LOGGER_UPDATE_TASK_MESSAGE, taskId);
        return taskService.updateTaskByUser(userId, taskId, userEventRequest);
    }

}
