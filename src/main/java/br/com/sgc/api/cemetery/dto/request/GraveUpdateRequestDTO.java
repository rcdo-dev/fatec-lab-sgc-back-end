package br.com.sgc.api.cemetery.dto.request;

import br.com.sgc.api.common.enums.AreaType;
import br.com.sgc.api.common.enums.GraveType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Grave Request Update", description = "DTO de entrada para atualização de dados da sepultura.")
public record GraveUpdateRequestDTO(
        @Schema(description = "Tipo da sepultura (EARTH / MAUSOLEUM).", example = "MAUSOLEUM")
        @NotNull(message = "{validation.assigned.value.cannot.be.null}")
        GraveType graveType,

        @Schema(description = "Quantidade de corpos suportados pela sepultura.", example = "4")
        @Positive(message = "{validation.required.positive.integer}")
        int bodyCapacity,

        @Schema(description = "Tipo de área da sepultura (COMMON / PERPETUAL).", example = "PERPETUAL")
        @NotNull(message = "{validation.assigned.value.cannot.be.null}")
        AreaType areaType) {

}
