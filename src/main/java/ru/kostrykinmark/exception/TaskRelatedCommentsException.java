package ru.kostrykinmark.exception;

public class TaskRelatedCommentsException extends RuntimeException {
    public TaskRelatedCommentsException(int taskId) {
        super(String.format("There are comments that has a task with id: %d", taskId));
    }
}
