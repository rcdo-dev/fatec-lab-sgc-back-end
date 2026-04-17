package br.com.sgc.api.cemetery.dto.request;

import br.com.sgc.api.common.enums.AreaType;
import br.com.sgc.api.common.enums.GraveType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Grave Request", description = "DTO de entrada para cadastro de sepultura.")
public record GraveRequestDTO(
        @Schema(description = "Número da sepultura.", example = "127")
        @Positive(message = "{validation.required.positive.integer}")
        int number,

        @Schema(description = "Tipo da sepultura (EARTH / MAUSOLEUM).", example = "EARTH")
        @NotNull(message = "{validation.assigned.value.cannot.be.null}")
        GraveType graveType,

        @Schema(description = "Quantidade de corpos suportados pela sepultura.", example = "2")
        @Positive(message = "{validation.required.positive.integer}")
        int bodyCapacity,

        @Schema(description = "Tipo de área da sepultura (COMMON / PERPETUAL).", example = "COMMON")
        @NotNull(message = "{validation.assigned.value.cannot.be.null}")
        AreaType areaType,

        @Schema(description = "ID da quadra em que a sepultura está vinculada.", example = "1")
        @NotNull(message = "{validation.assigned.value.cannot.be.null}")
        @Positive(message = "{validation.required.positive.integer}")
        Long blockId
) {

}
