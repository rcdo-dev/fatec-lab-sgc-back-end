package br.com.sgc.api.person.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import br.com.sgc.api.common.enums.DeceasedStatus;
import br.com.sgc.api.common.enums.GenderType;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Deceased Pet Response", description = "DTO de resposta para falecido do tipo Pet.")
@JsonPropertyOrder({"id", "name", "species", "breed", "sex", "color", "estimatedAge", "declarantId"})
public record DeceasedPetResponseDTO(
    @Schema(description = "Identificador único do falecido.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    Long id,

    @Schema(description = "Nome do pet falecido.", example = "Mel")
    String name,

    @Schema(description = "Espécie do animal.", example = "Cachorro")
    String species,

    @Schema(description = "Raça do animal.", example = "Pinscher")
    String breed,

    @Schema(description = "Gênero (MALE / FEMALE).", example = "FEMALE")
    GenderType sex,

    @Schema(description = "Cor.", example = "Marrom")
    String color,

    @Schema(description = "Idade estimada do animal.", example = "11")
    int estimatedAge,

    @Schema(description = "Gênero (ACTIVE / BURIED / EXHUMED / TRANSFERRED / ARCHIVED).", example = "ACTIVE")
    DeceasedStatus deceasedStatus,

    @Schema(description = "Este dado foi arquivado?", example = "false")
    boolean archived,

    @Schema(description = "Data do arquivamento.", example = "2026-04-23")
    LocalDateTime archivedAt,

    @Schema(description = "ID do declarante.", example = "1")
    Long declarantId
) {

}
