package ru.kostrykinmark.exception;

import java.time.LocalDateTime;

import static ru.kostrykinmark.comment.mapper.CommentMapper.formatter;

public class WrongDateRangeException extends RuntimeException {
    public WrongDateRangeException(LocalDateTime rangeStart, LocalDateTime rangeEnd) {
        super(String.format("Search query has range start %s and range end %s", rangeStart.format(formatter), rangeEnd.format(formatter)));
    }

}
