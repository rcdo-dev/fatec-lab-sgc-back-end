package br.com.sgc.api.cemetery.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import br.com.sgc.api.common.enums.ContractStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(title = "Contract Request", description = "DTO de entrada para cadastro de contrato.")
public record ContractRequestDTO(
        @Schema(description = "Numero unico do contrato.", example = "001")
        @NotBlank(message = "{validation.text.cannot.be.blank.or.null}")
        String number,

        @Schema(description = "Taxa do contrato.", example = "269.90")
        @NotNull(message = "{validation.assigned.value.cannot.be.null}")
        @Positive(message = "{validation.required.positive}")
        BigDecimal fee,

        @Schema(description = "Status inicial do contrato.", example = "ACTIVE")
        @NotNull(message = "{validation.assigned.value.cannot.be.null}")
        ContractStatus status,

        @Schema(description = "Data inicial da vigencia.", example = "2025-06-01")
        @NotNull(message = "{validation.assigned.value.cannot.be.null}")
        LocalDate startDate,

        @Schema(description = "Data final da vigencia.", example = "2026-06-01")
        @NotNull(message = "{validation.assigned.value.cannot.be.null}")
        LocalDate endDate,

        @Schema(description = "ID da quadra.", example = "1")
        @NotNull(message = "{validation.assigned.value.cannot.be.null}")
        @Positive(message = "{validation.required.positive.integer}")
        Long blockId,

        @Schema(description = "Dados do titular do contrato.")
        @NotNull(message = "{validation.assigned.value.cannot.be.null}")
        @Valid
        ContractHolderRequestDTO holder,

        @Schema(description = "ID de sepultura existente para vinculo.", example = "1")
        @Positive(message = "{validation.required.positive.integer}")
        Long graveId,

        @Schema(description = "Dados minimos para criacao de nova sepultura.")
        @Valid
        ContractGraveRequestDTO newGrave
) {

}
