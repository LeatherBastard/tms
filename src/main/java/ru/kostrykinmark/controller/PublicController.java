package ru.kostrykinmark.controller;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.kostrykinmark.comment.model.Comment;
import ru.kostrykinmark.comment.service.CommentService;
import ru.kostrykinmark.task.model.Task;
import ru.kostrykinmark.task.model.TaskPriority;
import ru.kostrykinmark.task.model.TaskStatus;
import ru.kostrykinmark.task.service.TaskService;
import ru.kostrykinmark.user.dto.SignInUserDto;
import ru.kostrykinmark.user.dto.SignUpUserDto;
import ru.kostrykinmark.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/")
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


    @GetMapping("/tasks/{authorId}")
    public List<Task> getTasksByAuthor(
            @PathVariable int authorId,
            @RequestParam(required = false) String annotation,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size

    ) {
        return taskService.getAllByAuthorId(authorId, annotation, description, status, priority, from, size);
    }

    @GetMapping("/tasks/{execId}")
    public List<Task> getTasksByExecutor(
            @PathVariable("execId") int executorId,
            @RequestParam(required = false) String annotation,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) TaskStatus status,
            @RequestParam(required = false) TaskPriority priority,
            @RequestParam(defaultValue = "0") int from,
            @RequestParam(defaultValue = "10") int size
    ) {
        return taskService.getAllByExecutorId(executorId, annotation, description, status, priority, from, size);
    }

    @GetMapping("/comments")
    public List<Comment> getComments(
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
