package br.com.sgc.api.burial.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import br.com.sgc.api.common.enums.BurialStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Burial Response", description = "DTO de resposta para o sepultamento.")
@JsonPropertyOrder({ "id", "date", "time", "status", "observations", "deceasedId", "graveId" })
public record BurialResponseDTO(
        @Schema(description = "Identificador único do sepultamento.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @Schema(description = "Data do sepultamento.", example = "2026-05-29")
        LocalDate date,

        @Schema(description = "Hora do sepultamento.", example = "10:30")
        LocalTime time,

        @Schema(description = "Status do sepultamento.", example = "IN_PROGRESS")
        BurialStatus status,

        @Schema(description = "Observações do sepultamento.", example = "Sepultamento realizado conforme protocolo.")
        String observations,

        @Schema(description = "Identificador do falecido.", example = "1")
        Long deceasedId,

        @Schema(description = "Identificador da sepultura.", example = "1")
        Long graveId) {
}
