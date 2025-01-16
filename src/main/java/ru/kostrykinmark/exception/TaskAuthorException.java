package ru.kostrykinmark.exception;

public class TaskAuthorException extends RuntimeException {
    public TaskAuthorException(int taskId, int userId) {
        super(String.format("User with id %d is not an author of the task %d", taskId, userId));
    }
}
