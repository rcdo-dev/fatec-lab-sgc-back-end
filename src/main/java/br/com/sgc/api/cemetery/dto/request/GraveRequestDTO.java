package br.com.sgc.api.cemetery.dto.request;

import br.com.sgc.api.common.enums.AreaType;
import br.com.sgc.api.common.enums.GraveType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Grave Request", description = "DTO de entrada para cadastro de sepultura.")
public record GraveRequestDTO(

        @Schema(description = "Número da sepultura.", example = "127") @Positive int number,

        @Schema(description = "Tipo da sepultura (EARTH / MAUSOLEUM).", example = "EARTH") @NotNull GraveType graveType,

        @Schema(description = "Quantidade de corpos suportados pela sepultura.", example = "2") @Positive int bodyCapacity,

        @Schema(description = "Tipo de área da sepultura (COMMON / PERPETUAL).", example = "COMMON") @NotNull AreaType areaType,

        @Schema(description = "ID da quadra em que a sepultura está vinculada.", example = "1") @NotNull @Positive Long blockId) {

}
