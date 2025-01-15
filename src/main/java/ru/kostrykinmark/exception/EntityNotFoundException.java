package ru.kostrykinmark.exception;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(String message, int id) {
        super(String.format(message, id));
    }
}
