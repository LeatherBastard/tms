package ru.kostrykinmark.exception;

public class TaskRelatedCommentsException extends RuntimeException {
    public TaskRelatedCommentsException(int taskId) {
        super(String.format("There are events that has a category with id: %d", taskId));
    }
}
