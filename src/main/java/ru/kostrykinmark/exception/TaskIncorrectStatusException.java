package ru.kostrykinmark.exception;

public class TaskIncorrectStatusException extends RuntimeException {
    public TaskIncorrectStatusException(String status) {
        super(String.format("There is no such task status as %s", status));
    }
}
