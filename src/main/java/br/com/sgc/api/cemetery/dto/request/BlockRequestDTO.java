package br.com.sgc.api.cemetery.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Block Request", description = "DTO de entrada para cadastro de quadras.")
public record BlockRequestDTO(
        @Schema(description = "Número da quadra.", example = "12")
        @Positive(message = "{validation.required.positive.integer}")
        int number,

        @Schema(description = "Descrição da quadra.", example = "Setor leste")
        String description,

        @Schema(description = "Indica se a quadra está ativa.", example = "true")
        boolean active,

        @Schema(description = "ID do cemitério em que a quadra está vinculada.", example = "1")
        @NotNull(message = "{validation.assigned.value.cannot.be.null}")
        @Positive(message = "{validation.required.positive.integer}")
        Long cemeteryId) {
}
