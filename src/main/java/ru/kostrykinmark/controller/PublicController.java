package ru.kostrykinmark.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
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
    private static final String SIGN_UP_OPERATION_SUMMARY = "Регистрация новых пользователей";
    private static final String SIGN_UP_METHOD_DESCRIPTION = "Создаёт новых пользователей, на вход принимает логин," +
            " электронную почту и пароль.";
    private static final String SIGN_IN_OPERATION_SUMMARY = "Авторизация пользователей";
    private static final String SIGN_IN_METHOD_DESCRIPTION = "Авторизует пользователей, выдавая им JWT токен," +
            " если учетная запись имеется в базе. В противном случае " +
            "выдаёт ошибку и отказывает в авторизации.";
    private static final String GET_TASKS_OPERATION_SUMMARY = "Получение задач по параметрам";
    private static final String GET_TASKS_METHOD_DESCRIPTION = "Позволяет получить задачи по параметрам,таким как: автор" +
            " задачи, исполнитель задачи, поиск по тексту, который встречается в названии или описании, статусы задач," +
            " приоритеты задач, с какого места и сколько задач должно быть отображено.";
    private static final String GET_COMMENTS_OPERATION_SUMMARY = "Получение комментариев по параметрам";
    private static final String GET_COMMENTS_OPERATION_DESCRIPTION = "Позволяет получить комментарии по параметрам,таким как:" +
            " поиск по тексту, который встречается в названии или описании, по задачам, которым принадлежат комментарии," +
            " по датам, когда были написаны комментарии и сколько комментариев должно быть отображено.";

    private static final String LOGGER_SIGN_UP_USER_MESSAGE = "Trying to sign up user: {} ";
    private static final String LOGGER_SIGN_IN_USER_MESSAGE = "Signing in user: {} ";
    private static final String LOGGER_GET_TASKS_MESSAGE = "Returning list of tasks with executor {}, " +
            " author {}, text {}, with statuses {}, priorities {}, from {} of size {}";
    private static final String LOGGER_GET_COMMENTS_MESSAGE = "Returning list of comments with text " +
            "{}, tasks {} , date start {}, date end {}, from {} of size {}";

    private final UserService userService;
    private final TaskService taskService;
    private final CommentService commentService;

    @Operation(summary = SIGN_UP_OPERATION_SUMMARY,
            description = SIGN_UP_METHOD_DESCRIPTION)
    @PostMapping("/signUp")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUpUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Пользователь для регистрации", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SignUpUserDto.class),
                            examples = @ExampleObject(value = "{ " +
                                    "\"username\": \"kostrykinmark\"," +
                                    "\"email\": \"kostrykinmark@gmail.com\"," +
                                    " \"password\": \"12345\"}")))
            @RequestBody @Validated SignUpUserDto user) {
        log.info(LOGGER_SIGN_UP_USER_MESSAGE, user);
        userService.signUpUser(user);
    }

    @Operation(summary = SIGN_IN_OPERATION_SUMMARY,
            description = SIGN_IN_METHOD_DESCRIPTION)
    @PostMapping("/signIn")
    @ResponseStatus(HttpStatus.OK)
    public String signInUser(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Пользователь для авторизации", required = true,
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = SignInUserDto.class),
                            examples = @ExampleObject(value = "{ \"email\": \"kostrykinmark@gmail.com\", \"password\": \"12345\"}")))
            @RequestBody @Validated SignInUserDto user) {
        log.info(LOGGER_SIGN_IN_USER_MESSAGE, user.getEmail());
        return userService.signInUser(user);
    }

    @Operation(summary = GET_TASKS_OPERATION_SUMMARY
            , description = GET_TASKS_METHOD_DESCRIPTION)
    @GetMapping("/tasks")
    public List<TaskFullDto> getTasks(
            @Parameter(description = "Идентификатор автора")
            @RequestParam(required = false) Integer authorId,
            @Parameter(description = "Идентификатор исполнителя")
            @RequestParam(required = false) Integer executorId,
            @Parameter(description = "Текст в названии или описании")
            @RequestParam(required = false) String text,
            @Parameter(description = "Статусы задач")
            @RequestParam(required = false) List<String> statuses,
            @Parameter(description = "Приоритеты задач")
            @RequestParam(required = false) List<String> priorities,
            @Parameter(description = "С какого элемента выводить")
            @RequestParam(defaultValue = "0") int from,
            @Parameter(description = "Сколько элементов требуется вывести")
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info(LOGGER_GET_TASKS_MESSAGE, authorId, executorId, text, statuses, priorities, from, size);
        return taskService.getAllTasks(authorId, executorId, text, statuses, priorities, from, size);
    }

    @Operation(summary = GET_COMMENTS_OPERATION_SUMMARY,
            description = GET_COMMENTS_OPERATION_DESCRIPTION)
    @GetMapping("/comments")
    public List<CommentResponseDto> getComments(
            @Parameter(description = "Текст в названии или описании")
            @RequestParam(required = false) String text,
            @Parameter(description = "Идентификаторы задач")
            @RequestParam(required = false) List<Integer> tasks,
            @Parameter(description = "Дата начала диапазона даты комментариев")
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @Parameter(description = "Дата завершения диапазона даты комментариев")
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @Parameter(description = "С какого элемента выводить")
            @RequestParam(defaultValue = "0") int from,
            @Parameter(description = "Сколько элементов требуется вывести")
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info(LOGGER_GET_COMMENTS_MESSAGE, text, tasks, rangeStart, rangeEnd, from, size);
        return commentService.getAllComments(text, tasks, rangeStart, rangeEnd, from, size);
    }
}
