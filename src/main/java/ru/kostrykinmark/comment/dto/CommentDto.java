package ru.kostrykinmark.comment.dto;

import lombok.*;
import ru.kostrykinmark.task.dto.TaskDto;
import ru.kostrykinmark.user.dto.UserDto;

@Data
@Builder
public class CommentDto {
    private TaskDto task;
    private UserDto user;
    private String text;
}
