package ru.kostrykinmark.exception;

public class UsernameOccupiedException extends RuntimeException {
    public UsernameOccupiedException(String username) {
        super(String.format("There is already a user, who has a username : %s", username));
    }
}
