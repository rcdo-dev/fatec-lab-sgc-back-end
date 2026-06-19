package br.com.sgc.api.cemetery.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Cemetery Response", description = "DTO de resposta para cemitério.")
@JsonPropertyOrder({ "id", "name", "foundation", "active", "wakeDurationMinutes", "wakeCharged", "wakeFee" })
public record CemeteryResponseDTO(
                @Schema(description = "Identificador único do cemitério.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
                Long id,

                @Schema(description = "Nome do cemitério.", example = "Cemitério do Cambiri.")
                String name,

                @Schema(description = "Data de fundação do cemitério.", example = "1983-01-01")
                LocalDate foundation,

                @Schema(description = "Indica se o cemitério está ativo.", example = "true")
                boolean active,

                @Schema(description = "Duração máxima do velório, em minutos.", example = "180")
                Integer wakeDurationMinutes,

                @Schema(description = "Indica se o cemitério cobra velório.", example = "false")
                Boolean wakeCharged,

                @Schema(description = "Valor cobrado pelo velório.", example = "150.00")
                BigDecimal wakeFee) {
}
