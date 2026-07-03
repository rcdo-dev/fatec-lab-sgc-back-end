package br.com.sgc.api.cemetery.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Positive;

@Schema(title = "Contract Grave Request", description = "DTO de entrada para criacao de sepultura via contrato.")
public record ContractGraveRequestDTO(
        @Schema(description = "Numero da sepultura.", example = "187")
        @Positive(message = "{validation.required.positive.integer}")
        int number,

        @Schema(description = "Quantidade de corpos suportados pela sepultura.", example = "4")
        @Positive(message = "{validation.required.positive.integer}")
        int bodyCapacity
) {

}
