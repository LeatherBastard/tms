package ru.kostrykinmark.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.kostrykinmark.comment.dto.CommentResponseDto;
import ru.kostrykinmark.comment.service.CommentService;
import ru.kostrykinmark.task.dto.TaskFullDto;
import ru.kostrykinmark.task.service.TaskService;
import ru.kostrykinmark.user.dto.SignInUserDto;
import ru.kostrykinmark.user.dto.SignUpUserDto;
import ru.kostrykinmark.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping(path = "/public")
@RequiredArgsConstructor
@Log4j2
public class PublicController {
    private static final String LOGGER_SIGN_UP_USER_MESSAGE = "Trying to sign up user: {} ";
    private static final String LOGGER_SIGN_IN_USER_MESSAGE = "Signing in user: {} ";
    private static final String LOGGER_GET_TASKS_MESSAGE = "Returning list of tasks with executor {}, " +
            " author {}, text {}, with statuses {}, priorities {}, from {} of size {}";
    private static final String LOGGER_GET_COMMENTS_MESSAGE = "Returning list of comments with text " +
            "{}, tasks {} , date start {}, date end {}, from {} of size {}";

    private final UserService userService;
    private final TaskService taskService;
    private final CommentService commentService;


    @PostMapping("/signUp")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUpUser(@RequestBody @Validated SignUpUserDto user) {
        log.info(LOGGER_SIGN_UP_USER_MESSAGE, user);
        userService.signUpUser(user);
    }

    @PostMapping("/signIn")
    @ResponseStatus(HttpStatus.OK)
    public String signInUser(@RequestBody @Validated SignInUserDto user) {
        log.info(LOGGER_SIGN_IN_USER_MESSAGE, user.getUsername());
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
        log.info(LOGGER_GET_TASKS_MESSAGE, authorId, executorId, text, statuses, priorities, from, size);
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
        log.info(LOGGER_GET_COMMENTS_MESSAGE, text, tasks, rangeStart, rangeEnd, from, size);
        return commentService.getAllComments(text, tasks, rangeStart, rangeEnd, from, size);
    }
}
