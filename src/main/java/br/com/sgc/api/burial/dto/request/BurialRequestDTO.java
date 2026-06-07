package br.com.sgc.api.burial.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

@Schema(title = "Burial Request", description = "DTO de entrada para cadastro de sepultamento.")
public record BurialRequestDTO(
        @Schema(description = "Data do sepultamento.", example = "2026-05-29")
        @NotNull(message = "{validation.assigned.value.cannot.be.null}")
        @PastOrPresent(message = "{validation.date.cannot.be.in.the.future}")
        LocalDate date,

        @Schema(description = "Hora do sepultamento.", example = "10:30")
        @NotNull(message = "{validation.assigned.value.cannot.be.null}")
        LocalTime time,

        @Schema(description = "Observações do sepultamento.", example = "Sepultamento realizado conforme protocolo.")
        @NotBlank(message = "{validation.text.cannot.be.blank.or.null}")
        String observations,

        @Schema(description = "Identificador do falecido.", example = "1")
        @NotNull(message = "{validation.assigned.value.cannot.be.null}")
        @Positive(message = "{validation.required.positive}")
        Long deceasedId,

        @Schema(description = "Identificador da sepultura.", example = "1")
        @NotNull(message = "{validation.assigned.value.cannot.be.null}")
        @Positive(message = "{validation.required.positive}")
        Long graveId) {
}
