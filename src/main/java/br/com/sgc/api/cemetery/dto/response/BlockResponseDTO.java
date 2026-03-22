package br.com.sgc.api.cemetery.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "number", "decription", "cemeteryId", "active" })
public record BlockResponseDTO(
                Long id,
                int number,
                String description,
                Long cemeteryId,
                boolean active) {

}
