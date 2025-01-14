package ru.kostrykinmark.comment.dto;

import lombok.*;
import ru.kostrykinmark.task.dto.NewTaskDto;

@Data
@Builder
public class CommentDto {
    private NewTaskDto task;
    private String text;
}
