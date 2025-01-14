package ru.kostrykinmark.task.service;

import ru.kostrykinmark.task.dto.NewTaskDto;
import ru.kostrykinmark.task.model.Task;

public interface TaskService {
    Task addTask(NewTaskDto task);


}
