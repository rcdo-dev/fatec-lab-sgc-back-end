package br.com.sgc.api.cemetery.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Cemetery Request", description = "DTO de entrada para cadastro de cemitério.")
public record CemeteryRequestDTO(

        @Schema(description = "Nome do cemitério.", example = "Cemitério do Cambiri")
        @NotBlank(message = "{validation.cemetery.name}")
        String name,

        @Schema(description = "Data de fundação do cemitério.", example = "1983-01-01")
        @NotNull(message = "{validation.assigned.value.cannot.be.null}")
        @PastOrPresent(message = "{validation.date.cannot.be.in.the.future}")
        LocalDate foundation,

        @Schema(description = "Indica se o cemitério está ativo.", example = "true")
        boolean active) {
}
