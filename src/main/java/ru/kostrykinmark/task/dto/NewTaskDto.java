package ru.kostrykinmark.task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.kostrykinmark.task.model.TaskPriority;


@Data
@Builder
public class NewTaskDto {
    private String title;

    private String description;

    private TaskPriority priority;

    private Integer executorId;

}
