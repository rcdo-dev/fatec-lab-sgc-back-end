package br.com.sgc.api.common.exception.classes;

public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}
