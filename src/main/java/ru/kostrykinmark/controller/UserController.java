package ru.kostrykinmark.controller;

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

    private static final String LOGGER_ADD_COMMENT_MESSAGE = "Adding comment to task with id {} : {} ";
    private static final String LOGGER_UPDATE_TASK_MESSAGE = "Updating task : {}";
    private final TaskService taskService;
    private final CommentService commentService;

    @PostMapping("/{userId}/tasks/{taskId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public CommentResponseDto addComment(@PathVariable("userId") int userId, @PathVariable("taskId") int taskId,
                                         @RequestBody @Validated NewCommentDto commentDto) {
        log.info(LOGGER_ADD_COMMENT_MESSAGE, taskId, commentDto);
        return commentService.add(userId, taskId, commentDto);
    }

    @PatchMapping("/{userId}/tasks/{taskId}")
    public TaskFullDto updateUserTask(@PathVariable("userId") int userId, @PathVariable("taskId") int taskId,
                                      @RequestBody @Validated UpdateTaskUserRequest userEventRequest) {
        log.info(LOGGER_UPDATE_TASK_MESSAGE, taskId);
        return taskService.updateTaskByUser(userId, taskId, userEventRequest);
    }

}
