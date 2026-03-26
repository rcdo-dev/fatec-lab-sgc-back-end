package br.com.sgc.api.common.exception.classes;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
