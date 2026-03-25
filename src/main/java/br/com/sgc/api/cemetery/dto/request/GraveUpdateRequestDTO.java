package br.com.sgc.api.cemetery.dto.request;

import br.com.sgc.api.common.enums.AreaType;
import br.com.sgc.api.common.enums.GraveType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Grave Request Update", description = "DTO de entrada para atualização de dados da sepultura.")
public record GraveUpdateRequestDTO(

        @Schema(description = "Tipo da sepultura (EARTH / MAUSOLEUM).", example = "MAUSOLEUM") @NotNull GraveType graveType,

        @Schema(description = "Quantidade de corpos suportados pela sepultura.", example = "4") @Positive int bodyCapacity,

        @Schema(description = "Tipo de área da sepultura (COMMON / PERPETUAL).", example = "PERPETUAL") @NotNull AreaType areaType) {

}
