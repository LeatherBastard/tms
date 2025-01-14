package ru.kostrykinmark.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {
    private static final String BAD_REQUEST_STATUS = "BAD_REQUEST";
    private static final String BAD_REQUEST_REASON = "Incorrectly made request.";

    private static final String UNAUTHORIZED_STATUS = "UNAUTHORIZED";
    private static final String UNAUTHORIZED_REASON = "You are not authorized to make such request";

    private static final String NOT_FOUND_STATUS = "NOT_FOUND";
    private static final String NOT_FOUND_REASON = "The required object was not found.";

    private static final String INTERNAL_SERVER_ERROR_STATUS = "INTERNAL_SERVER_ERROR";
    private static final String INTERNAL_SERVER_ERROR_REASON = "Internal server error.";


    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleUserNameOccupiedException(final UsernameOccupiedException exception) {
        return new ApiError(BAD_REQUEST_STATUS, BAD_REQUEST_REASON, exception.toString(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleUserEmailOccupiedException(final UserEmailOccupiedException exception) {
        return new ApiError(BAD_REQUEST_STATUS, BAD_REQUEST_REASON, exception.toString(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiError handleBadCredentialException(final BadCredentialsException exception) {
        return new ApiError(UNAUTHORIZED_STATUS, UNAUTHORIZED_REASON, exception.toString(), LocalDateTime.now());
    }

}
