package br.com.sgc.api.person.dto.response;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import br.com.sgc.api.common.enums.DeceasedStatus;
import br.com.sgc.api.common.enums.EyeType;
import br.com.sgc.api.common.enums.GenderType;
import br.com.sgc.api.common.enums.HairType;
import br.com.sgc.api.common.enums.SkinColor;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Deceased Unidentified Response", description = "DTO de resposta para falecido não identificado.")
@JsonPropertyOrder({"id", "estimatedAge", "gender", "skinColor", "stature", "hairColor", "hairType", "eyeColor", "eyeType", "declarantId"})
public record DeceasedUnidentifiedResponseDTO(
    @Schema(description = "Identificador único do falecido.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    Long id,
    
    @Schema(description = "Data estimada.", example = "97")
    int estimatedAge,

    @Schema(description = "Gênero (MALE / FEMALE).", example = "MALE")
    GenderType gender,

    @Schema(description = "Cor da pele (WHITE / BLACK / BROWN / YELLOW / INDIGENOUS).", example = "WHITE")
    SkinColor skinColor,

    @Schema(description = "Altura medida em metros.", example = "1.84")
    float stature,

    @Schema(description = "Cor do cabelo.", example = "Branco")
    String hairColor,

    @Schema(description = "Tipo do cabelo (STRAIGHT / WAVY / CURLY / KINKY).", example = "STRAIGHT")
    HairType hairType,
    
    @Schema(description = "Cor dos olhos.", example = "Verdes")
    String eyeColor,

    @Schema(description = "Tipo dos olhos (ALMOND_SHAPED / ROUND / DROOPY / DEEP_SET / PROTRUDING / ASIAN / ASYMMETRICAL).", example = "ROUND")
    EyeType eyeType,

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
