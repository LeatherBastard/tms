package ru.kostrykinmark.controller;


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
    private static final String LOGGER_ADD_TASK_MESSAGE = "Adding task: {}";
    private static final String LOGGER_GET_TASK_BY_ID_MESSAGE = "Getting task with id: {}";
    private static final String LOGGER_REMOVE_TASK_MESSAGE = "Removing task with id: {}";

    private static final String LOGGER_UPDATE_ADMIN_TASK_MESSAGE = "Updating task from admin with task id: {}";

    private static final String LOGGER_ADD_COMMENT_MESSAGE = "Adding comment to task with id {} : {} ";

    private final TaskService taskService;
    private final CommentService commentService;

    @PostMapping("/{adminId}/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskFullDto addTask(@PathVariable("adminId") int adminId, @RequestBody NewTaskDto taskDto) {
        log.info(LOGGER_ADD_TASK_MESSAGE, taskDto);
        return taskService.addTask(adminId, taskDto);
    }

    @GetMapping("/tasks/{taskId}")
    public TaskFullDto getTask(@PathVariable("taskId") int taskId) {
        log.info(LOGGER_GET_TASK_BY_ID_MESSAGE, taskId);
        return taskService.getById(taskId);
    }

    @DeleteMapping("/tasks/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable("taskId") int taskId) {
        log.info(LOGGER_REMOVE_TASK_MESSAGE, taskId);
        taskService.delete(taskId);
    }

    @PatchMapping("/tasks/{taskId}")
    public TaskFullDto updateTaskByAdmin(@PathVariable("taskId") int taskId, @RequestBody @Validated UpdateTaskAdminRequest adminTaskRequest) {
        log.info(LOGGER_UPDATE_ADMIN_TASK_MESSAGE, taskId);
        return taskService.updateTaskByAdmin(taskId, adminTaskRequest);
    }

    @GetMapping("/{adminId}/tasks/{taskId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public CommentResponseDto addComment(@PathVariable("adminId") int adminId, @PathVariable("taskId") int taskId, @RequestBody @Validated NewCommentDto commentDto) {
        log.info(LOGGER_ADD_COMMENT_MESSAGE, taskId, commentDto);
        return commentService.add(adminId, taskId, commentDto);
    }

}
