package com.dbybek.TaskManager.Exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "timestamp",
        "status",
        "error",
        "message",
        "errors",
        "path"
})
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int status;
    private String error;
    private String message;
    private Map<String, String> errors;
    private String path;

    public ErrorResponse(int status, String error, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
    }

    public ErrorResponse(int status, String error, Map<String, String> errors,String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.errors = errors;
        this.path = path;
    }

}
