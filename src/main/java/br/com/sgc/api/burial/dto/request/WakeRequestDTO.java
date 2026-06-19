package br.com.sgc.api.burial.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(title = "Wake Request", description = "DTO de entrada para agendamento de velório.")
public record WakeRequestDTO(
        @Schema(description = "Identificador do falecido.", example = "1")
        @NotNull(message = "{validation.assigned.value.cannot.be.null}")
        @Positive(message = "{validation.required.positive}")
        Long deceasedId,

        @Schema(description = "Identificador do cemitério. Opcional quando houver apenas um cemitério ativo.", example = "1")
        @Positive(message = "{validation.required.positive}")
        Long cemeteryId,

        @Schema(description = "Data do velório.", example = "2026-05-29")
        @NotNull(message = "{validation.assigned.value.cannot.be.null}")
        LocalDate date,

        @Schema(description = "Hora de início do velório.", example = "10:00")
        @NotNull(message = "{validation.assigned.value.cannot.be.null}")
        LocalTime startTime,

        @Schema(description = "Hora de término do velório.", example = "13:00")
        @NotNull(message = "{validation.assigned.value.cannot.be.null}")
        LocalTime endTime,

        @Schema(description = "Observações do velório.", example = "Família solicitou sala reservada.")
        String observations) {
}
