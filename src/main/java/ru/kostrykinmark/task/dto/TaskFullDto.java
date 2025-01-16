package ru.kostrykinmark.task.dto;

import lombok.Builder;
import lombok.Data;
import ru.kostrykinmark.task.model.TaskPriority;
import ru.kostrykinmark.task.model.TaskStatus;
import ru.kostrykinmark.user.dto.UserShortDto;

@Data
@Builder
public class TaskFullDto {
    private Integer id;
    private String title;
    private String description;
    private TaskStatus status;
    private TaskPriority priority;
    UserShortDto author;
    UserShortDto executor;
}
