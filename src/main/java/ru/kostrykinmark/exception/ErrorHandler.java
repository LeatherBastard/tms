package ru.kostrykinmark.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {
    private static final String BAD_REQUEST_STATUS = "BAD_REQUEST";
    private static final String BAD_REQUEST_REASON = "Incorrectly made request.";

    private static final String CONFLICT_STATUS = "CONFLICT";
    private static final String CONFLICT_REASON = "Incorrectly made request.";

    private static final String UNAUTHORIZED_STATUS = "UNAUTHORIZED";
    private static final String UNAUTHORIZED_REASON = "You are not authorized to make such request";

    private static final String NOT_FOUND_STATUS = "NOT_FOUND";
    private static final String NOT_FOUND_REASON = "The required object was not found.";

    private static final String INTERNAL_SERVER_ERROR_STATUS = "INTERNAL_SERVER_ERROR";
    private static final String INTERNAL_SERVER_ERROR_REASON = "Internal server error.";


    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleUserNameOccupiedException(final UsernameOccupiedException exception) {
        return new ApiError(ErrorHandler.CONFLICT_STATUS, ErrorHandler.CONFLICT_REASON, exception.toString(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleTaskIncorrectPriorityException(final TaskIncorrectPriorityException exception) {
        return new ApiError(BAD_REQUEST_STATUS, BAD_REQUEST_REASON, exception.toString(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleTaskAuthorException(final TaskAuthorException exception) {
        return new ApiError(BAD_REQUEST_STATUS, BAD_REQUEST_REASON, exception.toString(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleMethodArgumentNotValidException(final MethodArgumentNotValidException exception) {
        return new ApiError(BAD_REQUEST_STATUS, BAD_REQUEST_REASON, exception.toString(), LocalDateTime.now());

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleTaskIncorrectStatusException(final TaskIncorrectStatusException exception) {
        return new ApiError(BAD_REQUEST_STATUS, BAD_REQUEST_REASON, exception.toString(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleUserEmailOccupiedException(final UserEmailOccupiedException exception) {
        return new ApiError(CONFLICT_STATUS, CONFLICT_REASON, exception.toString(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleCategoryRelatedEventsException(final TaskRelatedCommentsException exception) {
        return new ApiError(CONFLICT_STATUS, CONFLICT_REASON, exception.toString(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiError handleBadCredentialException(final BadCredentialsException exception) {
        return new ApiError(UNAUTHORIZED_STATUS, UNAUTHORIZED_REASON, exception.toString(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleEntityNotFoundException(final EntityNotFoundException exception) {
        return new ApiError(NOT_FOUND_STATUS, NOT_FOUND_REASON, exception.getMessage(), LocalDateTime.now());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleEventWrongDateRangeException(final WrongDateRangeException exception) {
        return new ApiError(BAD_REQUEST_STATUS, BAD_REQUEST_REASON, exception.toString(), LocalDateTime.now());

    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleThrowable(final Throwable exception) {
        return new ApiError(INTERNAL_SERVER_ERROR_STATUS, INTERNAL_SERVER_ERROR_REASON, exception.toString(), LocalDateTime.now());
    }

}
