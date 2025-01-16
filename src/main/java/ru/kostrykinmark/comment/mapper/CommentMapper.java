package ru.kostrykinmark.comment.mapper;

import org.springframework.stereotype.Component;
import ru.kostrykinmark.comment.dto.CommentResponseDto;
import ru.kostrykinmark.comment.model.Comment;

import java.time.format.DateTimeFormatter;

@Component
public class CommentMapper {
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public CommentResponseDto mapToCommentDto(Comment comment) {
        return CommentResponseDto.builder()
                .id(comment.getId())
                .text(comment.getText())
                .created(comment.getCreated().format(formatter))
                .build();
    }
}
