package br.com.sgc.api.common.exception;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "API Erro Response", description = "Resposta padronizada de erro da API")
public record ApiErrorResponse(
        @Schema(description = "Data e hora em que o erro ocorreu.", example = "2026-03-26T10:15:30")
        LocalDateTime timestamp,

        @Schema(description = "Código HTTP da resposta.", example = "400")
        Integer status,

        @Schema(description = "Tipo resumido do erro.", example = "Business rule violation")
        String error,

        @Schema(description = "Mensagem detalhada do erro.", example = "Já existe um cemitério cadastrado com esse nome.")
        String message,

        @Schema(description = "Caminho da requisição que gerou o erro.", example = "/api/cemetery")
        String path) {
    public ApiErrorResponse(Integer status, String error, String message, String path) {
        this(LocalDateTime.now(), status, error, message, path);
    }
}
