package ru.kostrykinmark.controller;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.kostrykinmark.comment.dto.CommentResponseDto;
import ru.kostrykinmark.comment.service.CommentService;
import ru.kostrykinmark.task.dto.TaskFullDto;
import ru.kostrykinmark.task.model.TaskPriority;
import ru.kostrykinmark.task.model.TaskStatus;
import ru.kostrykinmark.task.service.TaskService;
import ru.kostrykinmark.user.dto.SignInUserDto;
import ru.kostrykinmark.user.dto.SignUpUserDto;
import ru.kostrykinmark.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/public")
public class PublicController {

    private final UserService userService;
    private final TaskService taskService;
    private final CommentService commentService;

    public PublicController(UserService userService, TaskService taskService, CommentService commentService) {
        this.userService = userService;
        this.taskService = taskService;
        this.commentService = commentService;
    }


    @PostMapping("/signUp")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUpUser(@RequestBody SignUpUserDto user) {
        userService.signUpUser(user);
    }


    @PostMapping("/signIn")
    @ResponseStatus(HttpStatus.OK)
    public String signInUser(@RequestBody SignInUserDto user) {
        return userService.signInUser(user);
    }


    @GetMapping("/tasks")
    public List<TaskFullDto> getTasks(
            @RequestParam(required = false) Integer authorId,
            @RequestParam(required = false) Integer executorId,
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<String> statuses,
            @RequestParam(required = false) List<String> priorities,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size

    ) {
        return taskService.getAllTasks(authorId, executorId, text, statuses, priorities, from, size);
    }


    @GetMapping("/comments")
    public List<CommentResponseDto> getComments(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Integer> tasks,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        return commentService.getAllComments(text, tasks, rangeStart, rangeEnd, from, size);
    }
}
