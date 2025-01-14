package ru.kostrykinmark.comment.mapper;

import org.springframework.stereotype.Component;
import ru.kostrykinmark.comment.dto.CommentDto;
import ru.kostrykinmark.comment.model.Comment;
import ru.kostrykinmark.task.dto.NewTaskDto;

@Component
public class CommentMapper {
    public CommentDto mapToCommentDto(Comment comment) {
        return CommentDto.builder()
                .task(
                        NewTaskDto.builder()

                                .build()
                )

                .text(comment.getText())
                .build();
    }
}
