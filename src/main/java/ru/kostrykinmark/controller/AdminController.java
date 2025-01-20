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
import ru.kostrykinmark.task.dto.NewTaskDto;
import ru.kostrykinmark.task.dto.TaskFullDto;
import ru.kostrykinmark.task.dto.UpdateTaskAdminRequest;
import ru.kostrykinmark.task.service.TaskService;

@RestController
@RequestMapping(path = "/admin")
@RequiredArgsConstructor
@Log4j2
public class AdminController {
    private static final String ADD_TASK_OPERATION_SUMMARY = "Добавление новых задач";
    private static final String ADD_TASK_METHOD_DESCRIPTION = "Добавляет новую задачу, на вход принимает пользователя." +
            "и задачу.";
    private static final String GET_TASK_OPERATION_SUMMARY = "Получение задач по идентификатору";
    private static final String GET_TASK_METHOD_DESCRIPTION = "Возвращает задачу, на вход принимает идентификатор задачи.";

    private static final String DELETE_TASK_OPERATION_SUMMARY = "Удаление задач по идентификатору";
    private static final String DELETE_TASK_METHOD_DESCRIPTION = "Удаляет задачу, на вход принимает идентификатор задачи.";

    private static final String PATCH_TASK_OPERATION_SUMMARY = "Обновление задачи";
    private static final String PATCH_TASK_METHOD_DESCRIPTION = "Обновляет пользовательскую задачу, на вход принимает" +
            " пользователя,задачу с параметрами для обновления.";

    private static final String ADD_COMMENT_OPERATION_SUMMARY = "Добавление комментария";
    private static final String ADD_COMMENT_METHOD_DESCRIPTION = "Добавляет новый комментарий, на вход принимает" +
            " пользователя,задачу и сам комментарий.";

    private static final String LOGGER_ADD_TASK_MESSAGE = "Adding task: {}";
    private static final String LOGGER_GET_TASK_BY_ID_MESSAGE = "Getting task with id: {}";
    private static final String LOGGER_REMOVE_TASK_MESSAGE = "Removing task with id: {}";

    private static final String LOGGER_UPDATE_ADMIN_TASK_MESSAGE = "Updating task from admin with task id: {}";

    private static final String LOGGER_ADD_COMMENT_MESSAGE = "Adding comment to task with id {} : {} ";

    private final TaskService taskService;
    private final CommentService commentService;

    @Operation(summary = ADD_TASK_OPERATION_SUMMARY,
            description = ADD_TASK_METHOD_DESCRIPTION)
    @PostMapping("/{adminId}/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskFullDto addTask(
            @Parameter(description = "Идентификатор админа")
            @PathVariable("adminId") int adminId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Новая задача", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NewTaskDto.class),
                            examples = @ExampleObject(value = "{ " +
                                    "\"title\": \"Do something fast please\"," +
                                    "\"description\": \"Do this accurate and fast\"," +
                                    "\"priority\": \"MEDIUM\"," +
                                    "\"executorId\": \"2\"" +
                                    "}")))
            @RequestBody NewTaskDto taskDto) {
        log.info(LOGGER_ADD_TASK_MESSAGE, taskDto);
        return taskService.addTask(adminId, taskDto);
    }

    @Operation(summary = GET_TASK_OPERATION_SUMMARY,
            description = GET_TASK_METHOD_DESCRIPTION)
    @GetMapping("/tasks/{taskId}")
    public TaskFullDto getTask(
            @Parameter(description = "Идентификатор задачи")
            @PathVariable("taskId") int taskId) {
        log.info(LOGGER_GET_TASK_BY_ID_MESSAGE, taskId);
        return taskService.getById(taskId);
    }

    @Operation(summary = DELETE_TASK_OPERATION_SUMMARY,
            description = DELETE_TASK_METHOD_DESCRIPTION)
    @DeleteMapping("/tasks/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(
            @Parameter(description = "Идентификатор задачи")
            @PathVariable("taskId") int taskId) {
        log.info(LOGGER_REMOVE_TASK_MESSAGE, taskId);
        taskService.delete(taskId);
    }

    @Operation(summary = PATCH_TASK_OPERATION_SUMMARY,
            description = PATCH_TASK_METHOD_DESCRIPTION)
    @PatchMapping("/tasks/{taskId}")
    public TaskFullDto updateTaskByAdmin(
            @Parameter(description = "Идентификатор задачи")
            @PathVariable("taskId") int taskId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Задача для обновления", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = UpdateTaskAdminRequest.class),
                            examples = @ExampleObject(value = "{ " +
                                    "\"title\": \"Do something fast please\"," +
                                    "\"description\": \"Do this accurate and fast\"," +
                                    "\"status\": \"FINISHED\"," +
                                    "\"priority\": \"MEDIUM\"," +
                                    "\"executor\": \"2\"" +
                                    "}")))
            @RequestBody @Validated UpdateTaskAdminRequest adminTaskRequest) {
        log.info(LOGGER_UPDATE_ADMIN_TASK_MESSAGE, taskId);
        return taskService.updateTaskByAdmin(taskId, adminTaskRequest);
    }

    @Operation(summary = ADD_COMMENT_OPERATION_SUMMARY,
            description = ADD_COMMENT_METHOD_DESCRIPTION)
    @GetMapping("/{adminId}/tasks/{taskId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public CommentResponseDto addComment(
            @Parameter(description = "Идентификатор админа")
            @PathVariable("adminId") int adminId,
            @Parameter(description = "Идентификатор задачи")
            @PathVariable("taskId") int taskId,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Комментарий для добавления", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = NewCommentDto.class),
                            examples = @ExampleObject(value = "{ \"text\": \"My comment\"}")))
            @RequestBody @Validated NewCommentDto commentDto) {
        log.info(LOGGER_ADD_COMMENT_MESSAGE, taskId, commentDto);
        return commentService.add(adminId, taskId, commentDto);
    }

}
