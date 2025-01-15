package ru.kostrykinmark.controller;


import org.springframework.http.HttpStatus;
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
public class AdminController {
    private final TaskService taskService;
    private final CommentService commentService;

    public AdminController(TaskService taskService, CommentService commentService) {
        this.taskService = taskService;
        this.commentService = commentService;
    }


    @PostMapping("{adminId}/tasks/")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskFullDto addTask(@PathVariable("adminId") int adminId, NewTaskDto taskDto) {
        return taskService.addTask(adminId,taskDto);
    }

    @GetMapping("/tasks/{taskId}")
    public TaskFullDto getTask(@PathVariable("taskId") int taskId) {
        return taskService.getById(taskId);
    }

    @DeleteMapping("/tasks/{taskId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(@PathVariable("taskId") int taskId) {
        taskService.delete(taskId);
    }

    @PatchMapping("/tasks/{taskId}")
    public TaskFullDto updateTask(@PathVariable("taskId") int taskId, @RequestBody UpdateTaskAdminRequest adminTaskRequest) {
        return taskService.updateEventByAdmin(taskId, adminTaskRequest);
    }

    @GetMapping("/tasks/{taskId}/comments")
    @ResponseStatus(HttpStatus.OK)
    public CommentResponseDto addComment(@PathVariable("taskId") int taskId, @RequestBody NewCommentDto commentDto) {
        return commentService.addComment(taskId, commentDto);
    }


}
