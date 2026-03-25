package br.com.sgc.api.cemetery.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Block Response", description = "DTO de resposta para quadra.")
@JsonPropertyOrder({ "id", "number", "decription", "active", "cemeteryId" })
public record BlockResponseDTO(

        @Schema(description = "Identificador único da quadra.", example = "1", accessMode = Schema.AccessMode.READ_ONLY) Long id,

        @Schema(description = "Número da quadra.", example = "12") int number,

        @Schema(description = "Descrição da quadra.", example = "Setor leste") String description,

        @Schema(description = "Indica se a quadra está ativa.", example = "true") boolean active,

        @Schema(description = "ID do cemitério em que a quadra está vinculada.", example = "1") Long cemeteryId) {

}
