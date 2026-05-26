package br.com.sgc.api.person.dto.request;

import java.time.LocalDateTime;

import br.com.sgc.api.common.enums.DeceasedStatus;
import br.com.sgc.api.common.enums.GenderType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Deceased Pet Request", description = "DTO de entrada para pet falecido.")
public record DeceasedPetRequestDTO(
    @Schema(description = "Nome do pet falecido.", example = "Mel")
    @NotBlank(message = "{validation.text.cannot.be.blank.or.null}")
    String name,

    @Schema(description = "Espécie do animal.", example = "Cachorro")
    @NotBlank(message = "{validation.text.cannot.be.blank.or.null}")
    String species,

    @Schema(description = "Raça do animal.", example = "Pinscher")
    @NotBlank(message = "{validation.text.cannot.be.blank.or.null}")
    String breed,

    @Schema(description = "Gênero (MALE / FEMALE).", example = "FEMALE")
    @NotNull(message = "{validation.assigned.value.cannot.be.null}")
    GenderType sex,

    @Schema(description = "Cor.", example = "Marrom")
    @NotBlank(message = "{validation.text.cannot.be.blank.or.null}")
    String color,

    @Schema(description = "Idade estimada do animal.", example = "11")
    @NotNull(message = "{validation.assigned.value.cannot.be.null}")
    @Positive(message = "{validation.required.positive}")
    int estimatedAge,

    @Schema(description = "Gênero (ACTIVE / BURIED / EXHUMED / TRANSFERRED / ARCHIVED).", example = "ACTIVE")
    @NotNull(message = "{validation.assigned.value.cannot.be.null}")
    DeceasedStatus deceasedStatus,

    @Schema(description = "Este dado foi arquivado?", example = "false")
    @NotNull(message = "{validation.assigned.value.cannot.be.null}")
    boolean archived,

    @Schema(description = "Data do arquivamento.", example = "2026-04-23")
    @NotNull(message = "{validation.assigned.value.cannot.be.null}")
    LocalDateTime archivedAt,

    @Schema(description = "ID do declarante.", example = "1")
    @NotNull(message = "{validation.assigned.value.cannot.be.null}")
    @Positive(message = "{validation.required.positive}")
    Long declarantId
) {

}
