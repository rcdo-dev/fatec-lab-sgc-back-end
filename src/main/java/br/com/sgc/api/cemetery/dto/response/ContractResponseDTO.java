package br.com.sgc.api.cemetery.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import br.com.sgc.api.common.enums.ContractStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Contract Response", description = "DTO de resposta para contrato.")
@JsonPropertyOrder({
        "id",
        "number",
        "fee",
        "status",
        "startDate",
        "endDate",
        "holder",
        "graveId",
        "blockId"
})
public record ContractResponseDTO(
        @Schema(description = "Identificador unico do contrato.", example = "1")
        Long id,

        @Schema(description = "Numero unico do contrato.", example = "001")
        String number,

        @Schema(description = "Taxa do contrato.", example = "269.90")
        BigDecimal fee,

        @Schema(description = "Status do contrato.", example = "ACTIVE")
        ContractStatus status,

        @Schema(description = "Data inicial da vigencia.", example = "2025-06-01")
        LocalDate startDate,

        @Schema(description = "Data final da vigencia.", example = "2026-06-01")
        LocalDate endDate,

        @Schema(description = "Titular do contrato.")
        ContractHolderResponseDTO holder,

        @Schema(description = "ID da sepultura vinculada.", example = "1")
        Long graveId,

        @Schema(description = "ID da quadra da sepultura.", example = "1")
        Long blockId
) {

}
