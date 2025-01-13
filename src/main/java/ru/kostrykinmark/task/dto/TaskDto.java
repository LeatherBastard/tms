package ru.kostrykinmark.task.dto;

import lombok.*;
import ru.kostrykinmark.task.model.TaskPriority;
import ru.kostrykinmark.task.model.TaskStatus;
import ru.kostrykinmark.user.dto.UserDto;


@Data
@Builder
public class TaskDto {
    private String annotation;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;

    private UserDto author;
    private UserDto executor;
}
