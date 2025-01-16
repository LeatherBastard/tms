package ru.kostrykinmark.exception;

public class TaskIncorrectPriorityException extends RuntimeException {
    public TaskIncorrectPriorityException(String priority) {
        super(String.format("There is no such task priority as %s", priority));
    }
}
