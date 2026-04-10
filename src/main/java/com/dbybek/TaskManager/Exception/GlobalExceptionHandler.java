package com.dbybek.TaskManager.Exception;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // ✅ Task Not Found
    @ExceptionHandler(TaskNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleTaskNotFound(TaskNotFoundException ex, HttpServletRequest request){
        log.error("Exception occurred", ex);
        return new ErrorResponse(
                404,
                "Not Found",
                ex.getMessage(),
                request.getRequestURI()
        );
    }

    // ✅ Validation Errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        log.error("Exception occurred", ex);

        return new ErrorResponse(
                400,
                "Bad Request",
                errors,
                request.getRequestURI()
        );
    }

    // ✅ Login Failure
    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleBadCredentials(BadCredentialsException ex,
                                              HttpServletRequest request) {
        log.error("Exception occurred", ex);
        return new ErrorResponse(
                401,
                "Unauthorized",
                "Invalid username or password",
                request.getRequestURI()
        );
    }

    // ✅ Access Denied (Role issue)
    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDenied(AccessDeniedException ex,
                                            HttpServletRequest request) {
        log.error("Exception occurred", ex);
        return new ErrorResponse(
                403,
                "Forbidden",
                "You do not have permission to access this resource",
                request.getRequestURI()
        );
    }

    // ✅ Fallback (VERY IMPORTANT)
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleGeneralException(Exception ex,
                                                HttpServletRequest request) {
        log.error("Exception occurred", ex);
        return new ErrorResponse(
                500,
                "Internal Server Error",
                "Something went wrong",
                request.getRequestURI()
        );
    }
}
