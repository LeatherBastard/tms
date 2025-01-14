package ru.kostrykinmark.task.dto;

import lombok.*;
import ru.kostrykinmark.task.model.TaskPriority;
import ru.kostrykinmark.task.model.TaskStatus;
import ru.kostrykinmark.user.dto.ShortUserDto;


@Data
@Builder
public class NewTaskDto {
    private String annotation;

    private String description;

    private TaskStatus status;

    private TaskPriority priority;

    private Integer authorId;

    private Integer executorId;

}
