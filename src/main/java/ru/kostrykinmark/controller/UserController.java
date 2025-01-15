package ru.kostrykinmark.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.kostrykinmark.comment.dto.CommentResponseDto;
import ru.kostrykinmark.comment.dto.NewCommentDto;
import ru.kostrykinmark.comment.service.CommentService;
import ru.kostrykinmark.task.dto.TaskFullDto;
import ru.kostrykinmark.task.dto.UpdateTaskUserRequest;
import ru.kostrykinmark.task.service.TaskService;

@RestController
@RequestMapping(path = "/users")
public class UserController {
    private final TaskService taskService;
    private final CommentService commentService;

    public UserController(TaskService taskService, CommentService commentService) {
        this.taskService = taskService;
        this.commentService = commentService;
    }

    @PostMapping("/{userId}/tasks/{taskId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public CommentResponseDto addComment(@PathVariable("userId") int userId, @PathVariable("taskId") int taskId,
                                         @RequestBody NewCommentDto commentDto) {
        return commentService.add(userId, taskId, commentDto);
    }

    @PatchMapping("/{userId}/tasks/{taskId}")
    public TaskFullDto updateUserTask(@PathVariable("userId") int userId, @PathVariable("taskId") int taskId,
                                      @RequestBody UpdateTaskUserRequest userEventRequest) {
        return taskService.updateTaskByUser(userId, taskId, userEventRequest);
    }


}
