package br.com.sgc.api.common.exception;

import br.com.sgc.api.common.exception.classes.BusinessException;
import br.com.sgc.api.common.exception.classes.ConflictException;
import br.com.sgc.api.common.exception.classes.ResourceNotFoundException;

import java.util.Objects;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import jakarta.servlet.http.HttpServletRequest;

import lombok.RequiredArgsConstructor;

@RestControllerAdvice // Esta anotação diz ao Spring: "Observe todos os controllers!".
@RequiredArgsConstructor
public class GlobalExceptionHandler {
        private final MessageSource messageSource;

        // Para regra de negócio.
        @ExceptionHandler(BusinessException.class)
        public ResponseEntity<ApiErrorResponse> handleBusinessException(BusinessException ex,
                        HttpServletRequest request) {
                ApiErrorResponse response = new ApiErrorResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                getMessage("exception.error.business.rule.violation"),
                                ex.getMessage(),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Para erro recurso não encontrado.
        @ExceptionHandler(ResourceNotFoundException.class)
        public ResponseEntity<ApiErrorResponse> handleResourceNotFound(ResourceNotFoundException ex,
                        HttpServletRequest request) {
                ApiErrorResponse response = new ApiErrorResponse(
                                HttpStatus.NOT_FOUND.value(),
                                getMessage("exception.error.resource.not.found"),
                                ex.getMessage(),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }

        // Para conflito nos dados.
        @ExceptionHandler(ConflictException.class)
        public ResponseEntity<ApiErrorResponse> handleConflictException(ConflictException ex,
                        HttpServletRequest request) {
                ApiErrorResponse response = new ApiErrorResponse(
                                HttpStatus.CONFLICT.value(),
                                getMessage("exception.error.conflict.business.rule"),
                                ex.getMessage(),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        // Para JSON malformado ou tipo inválido.
        @ExceptionHandler(HttpMessageNotReadableException.class)
        public ResponseEntity<ApiErrorResponse> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
                        HttpServletRequest request) {
                ApiErrorResponse response = new ApiErrorResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                getMessage("exception.error.malformed.request"),
                                getMessage("exception.message.request.body.invalid"),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        // Para erro de validação com @Valid.
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<ApiErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                        HttpServletRequest request) {
                ApiErrorResponse response = new ApiErrorResponse(
                                HttpStatus.BAD_REQUEST.value(),
                                getMessage("exception.error.invalid.fields"),
                                getMessage("exception.message.fields.contain.invalid.data"),
                                request.getRequestURI());

                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }

        private String getMessage(String key) {
        return messageSource.getMessage(Objects.requireNonNull(key), null, "Messagem nao encontrada: " + key,
                LocaleContextHolder.getLocale());
    }
}
