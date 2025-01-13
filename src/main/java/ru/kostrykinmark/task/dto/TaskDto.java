package ru.kostrykinmark.task.dto;

import lombok.*;
import ru.kostrykinmark.task.model.TaskPriority;
import ru.kostrykinmark.task.model.TaskStatus;
import ru.kostrykinmark.user.dto.UserDto;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class TaskDto {
    private int id;

    private String annotation;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;

    private UserDto author;
    private UserDto executor;
}
