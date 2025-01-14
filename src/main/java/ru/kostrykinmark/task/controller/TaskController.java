package ru.kostrykinmark.task.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kostrykinmark.task.dto.NewTaskDto;
import ru.kostrykinmark.task.model.Task;
import ru.kostrykinmark.task.service.TaskService;

@RestController
@RequestMapping(path = "/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }


    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public Task addTask(@RequestBody NewTaskDto task) {
        return taskService.addTask(task);
    }


}
