package br.com.sgc.api.cemetery.dto.request;

import br.com.sgc.api.common.enums.AreaType;
import br.com.sgc.api.common.enums.GraveType;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record GraveRequestDTO(
        @Positive int number,
        @NotNull GraveType graveType,
        @Positive int bodyCapacity,
        @NotNull AreaType areaType,
        boolean active,
        @NotNull @Positive Long blockId) {

}
