package br.com.sgc.api.burial.dto.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import br.com.sgc.api.common.enums.DecompositionStatus;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Exhumation Inspection Response", description = "DTO de resposta para a inspeção.")
@JsonPropertyOrder({ "id", "inspectedAt", "decompositionStatus", "nextEligibleDate", "observations", "burialId" })
public record ExhumationInspectionResponseDTO(
    @Schema(description = "Identificador único da inspeção.", example = "27", accessMode = Schema.AccessMode.READ_ONLY)
    Long id,

    @Schema(description = "Data da inspeção.", example = "2026-08-04")
    LocalDate inspectedAt,

    @Schema(description = "Status de decomposição dos restos mortais.", example = "INCOMPLETE")
    DecompositionStatus decompositionStatus,

    @Schema(description = "Data da próxima inspeção.", example = "2028-08-04")
    LocalDate nextEligibleDate,

    @Schema(description = "Observações da inspeção.", example = "Restos mortais ainda em decomposição.")
    String observations,

    @Schema(description = "Identificador do sepultamento.", example = "27")
    Long burialId
) {
}
