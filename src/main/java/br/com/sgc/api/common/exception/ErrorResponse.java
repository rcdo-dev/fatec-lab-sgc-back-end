package br.com.sgc.api.common.exception;

import java.time.LocalDateTime;

public record ErrorResponse(
        LocalDateTime timestamp,
        Integer status,
        String error,
        String message,
        String path) {
    public ErrorResponse(Integer status, String error, String message, String path) {
        this(LocalDateTime.now(), status, error, message, path);
    }
}
