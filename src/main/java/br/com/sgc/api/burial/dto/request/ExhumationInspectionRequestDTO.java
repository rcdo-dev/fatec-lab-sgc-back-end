package br.com.sgc.api.burial.dto.request;

import br.com.sgc.api.common.enums.DecompositionStatus;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

@Schema(title = "Exhumation Inspection Request", description = "DTO de entrada para registro de inspeção.")
public record ExhumationInspectionRequestDTO(
    @Schema(description = "Data da inspeção.", example = "2026-08-04")
    @NotNull(message = "{validation.assigned.value.cannot.be.null}")
    @PastOrPresent(message = "{validation.date.cannot.be.in.the.future}")
    LocalDate inspectedAt,

    @Schema(description = "Status de decomposição dos restos mortais.", example = "INCOMPLETE")
    @NotNull(message = "{validation.assigned.value.cannot.be.null}")
    DecompositionStatus decompositionStatus,

    @Schema(description = "Data da próxima inspeção.", example = "2028-08-04")
    @Future(message = "{validation.date.must.be.in.the.future}")
    LocalDate nextEligibleDate,

    @Schema(description = "Observações da inspeção.", example = "Restos mortais ainda em decomposição.")
    String observations,

    @Schema(description = "Identificador do sepultamento.", example = "27")
    @NotNull(message = "{validation.assigned.value.cannot.be.null}")
    @Positive(message = "{validation.required.positive}")
    Long burialId
) {
}
