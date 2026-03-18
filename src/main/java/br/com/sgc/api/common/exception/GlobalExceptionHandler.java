package br.com.sgc.api.common.exception;

import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice // Esta anotação diz ao Spring: "Observe todos os controllers!".
@RequiredArgsConstructor
public class GlobalExceptionHandler {
    private final MessageSource messageSource;
}
