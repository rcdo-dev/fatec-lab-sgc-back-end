package br.com.sgc.api.cemetery.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(title = "Contract Update Request", description = "DTO de entrada para atualizacao de contrato.")
public record ContractUpdateRequestDTO(
        @Schema(description = "Numero unico do contrato.", example = "001")
        @NotBlank(message = "{validation.text.cannot.be.blank.or.null}")
        String number,

        @Schema(description = "Taxa do contrato.", example = "269.90")
        @NotNull(message = "{validation.assigned.value.cannot.be.null}")
        @Positive(message = "{validation.required.positive}")
        BigDecimal fee,

        @Schema(description = "Data inicial da vigencia.", example = "2025-06-01")
        @NotNull(message = "{validation.assigned.value.cannot.be.null}")
        LocalDate startDate,

        @Schema(description = "Data final da vigencia.", example = "2026-06-01")
        @NotNull(message = "{validation.assigned.value.cannot.be.null}")
        LocalDate endDate,

        @Schema(description = "Dados do titular do contrato.")
        @NotNull(message = "{validation.assigned.value.cannot.be.null}")
        @Valid
        ContractHolderRequestDTO holder
) {

}
