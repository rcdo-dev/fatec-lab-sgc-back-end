package br.com.sgc.api.burial.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import br.com.sgc.api.common.enums.WakeStatus;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Wake Response", description = "DTO de resposta para agendamento de velório.")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "date", "startTime", "endTime", "status", "appliedFee", "observations", "deceasedId",
        "cemeteryId", "message" })
public record WakeResponseDTO(
        @Schema(description = "Identificador único do velório.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
        Long id,

        @Schema(description = "Data do velório.", example = "2026-05-29")
        LocalDate date,

        @Schema(description = "Hora de início do velório.", example = "10:00")
        LocalTime startTime,

        @Schema(description = "Hora de término do velório.", example = "13:00")
        LocalTime endTime,

        @Schema(description = "Status do velório.", example = "SCHEDULED")
        WakeStatus status,

        @Schema(description = "Taxa aplicada no momento do agendamento.", example = "150.00")
        BigDecimal appliedFee,

        @Schema(description = "Observações do velório.", example = "Família solicitou sala reservada.")
        String observations,

        @Schema(description = "Identificador do falecido.", example = "1")
        Long deceasedId,

        @Schema(description = "Identificador do cemitério.", example = "1")
        Long cemeteryId,

        @Schema(description = "Mensagem de sucesso.", example = "Velório registrado com sucesso.")
        String message) {
}
