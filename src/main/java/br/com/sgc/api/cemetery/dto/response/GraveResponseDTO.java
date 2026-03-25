package br.com.sgc.api.cemetery.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import br.com.sgc.api.common.enums.AreaType;
import br.com.sgc.api.common.enums.GraveStatus;
import br.com.sgc.api.common.enums.GraveType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Grave Response", description = "DTO de resposta para sepultura.")
@JsonPropertyOrder({
                "id",
                "number",
                "graveType",
                "bodyCapacity",
                "areaType",
                "status",
                "blocked",
                "reason",
                "blockId"
})
public record GraveResponseDTO(

                @Schema(description = "Identificador único da sepultura.", example = "1") Long id,

                @Schema(description = "Número da sepultura.", example = "127") int number,

                @Schema(description = "Tipo da sepultura (EARTH / MAUSOLEUM).", example = "EARTH") GraveType graveType,

                @Schema(description = "Quantidade de corpos suportados pela sepultura.", example = "2") int bodyCapacity,

                @Schema(description = "Tipo de área da sepultura (COMMON / PERPETUAL).", example = "COMMON") AreaType areaType,

                @Schema(description = "Indica se a sepultura está ativa.", example = "true") boolean active,

                @Schema(description = "Define o status da sepultura (AVAILABLE, OCCUPIED, MAINTENANCE).", example = "OCCUPIED") GraveStatus status,

                @Schema(description = "Informa se a sepultura está bloqueada.", example = "true") boolean blocked,

                @Schema(description = "Descreve a razão/motivo do bloqueio da sepultura.", example = "Processo jurídico aguardando resposta.") String reason,

                @Schema(description = "ID da quadra em que a sepultura está vinculada.", example = "1") Long blockId) {

}
