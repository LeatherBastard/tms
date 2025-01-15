package ru.kostrykinmark.comment.service;

import ru.kostrykinmark.comment.dto.CommentResponseDto;
import ru.kostrykinmark.comment.dto.NewCommentDto;
import ru.kostrykinmark.comment.model.Comment;

import java.time.LocalDateTime;
import java.util.List;

public interface CommentService {
    CommentResponseDto addComment(int taskId, NewCommentDto commentDto);

    List<CommentResponseDto> getAllComments(String text, List<Integer> tasks, LocalDateTime rangeStart, LocalDateTime rangeEnd, int from, int size);

    CommentResponseDto add(int userId, int taskId, NewCommentDto commentDto);
}
