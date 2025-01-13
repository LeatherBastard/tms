package ru.kostrykinmark.comment.mapper;

import org.springframework.stereotype.Component;
import ru.kostrykinmark.comment.dto.CommentDto;
import ru.kostrykinmark.comment.model.Comment;
import ru.kostrykinmark.task.dto.TaskDto;
import ru.kostrykinmark.user.dto.UserDto;

@Component
public class CommentMapper {
    public CommentDto mapToCommentDto(Comment comment) {
        return CommentDto.builder()
                .task(
                        TaskDto.builder()
                                .annotation(comment.getTask().getAnnotation())
                                .description(comment.getTask().getDescription())
                                .status(comment.getTask().getStatus())
                                .priority(comment.getTask().getPriority())
                                .author(UserDto.builder()
                                        .name(comment.getTask().getAuthor().getName())
                                        .email(comment.getTask().getAuthor().getEmail())
                                        .build())
                                .build()
                )
                .user(
                        UserDto.builder()
                                .name(comment.getUser().getName())
                                .email(comment.getUser().getEmail())
                                .build()
                )
                .text(comment.getText())
                .build();
    }
}
