package ru.kostrykinmark.comment.dto;

import lombok.Builder;
import lombok.Data;
import ru.kostrykinmark.task.dto.TaskShortDto;
import ru.kostrykinmark.user.dto.UserShortDto;

@Data
@Builder
public class CommentResponseDto {
    private Integer id;
    private UserShortDto user;
    private TaskShortDto task;
    private String text;
    private String created;
}
