package br.com.sgc.api.cemetery.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import br.com.sgc.api.common.enums.AreaType;
import br.com.sgc.api.common.enums.GraveType;

@JsonPropertyOrder({ "id", "number", "graveType", "bodyCapacity", "areaType", "blockId" })
public record GraveResponseDTO(
        Long id,
        int number,
        GraveType graveType,
        int bodyCapacity,
        AreaType areaType,
        Long blockId) {

}
