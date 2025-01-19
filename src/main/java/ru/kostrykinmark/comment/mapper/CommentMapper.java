package ru.kostrykinmark.comment.mapper;

import org.springframework.stereotype.Component;
import ru.kostrykinmark.comment.dto.CommentResponseDto;
import ru.kostrykinmark.comment.model.Comment;
import ru.kostrykinmark.task.dto.TaskShortDto;
import ru.kostrykinmark.user.dto.UserShortDto;

import java.time.format.DateTimeFormatter;

@Component
public class CommentMapper {
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public CommentResponseDto mapToCommentDto(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .user(UserShortDto.builder()
                        .id(comment.getUser().getId())
                        .username(comment.getUser().getUsername())
                        .build())
                .task(TaskShortDto.builder()
                        .id(comment.getTask().getId())
                        .title(comment.getTask().getTitle())
                        .status(comment.getTask().getStatus())
                        .priority(comment.getTask().getPriority())
                        .author(
                                UserShortDto.builder()
                                        .id(comment.getTask().getAuthor().getId())
                                        .username(comment.getTask().getAuthor().getUsername())
                                        .build()
                        )
                        .executor(
                                UserShortDto.builder()
                                        .id(comment.getTask().getExecutor().getId())
                                        .username(comment.getTask().getExecutor().getUsername())
                                        .build()
                        )
                        .build())
                .created(comment.getCreated().format(formatter))
                .build();
    }
}
