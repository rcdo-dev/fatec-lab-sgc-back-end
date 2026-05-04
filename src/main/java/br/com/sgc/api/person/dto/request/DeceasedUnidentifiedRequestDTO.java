package br.com.sgc.api.person.dto.request;

import br.com.sgc.api.common.enums.EyeType;
import br.com.sgc.api.common.enums.GenderType;
import br.com.sgc.api.common.enums.HairType;
import br.com.sgc.api.common.enums.SkinColor;
import br.com.sgc.api.person.entity.DeclarantEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Schema(title = "Deceased Unidentified Request", description = "DTO de entrada o falecido não identificado.")
public record DeceasedUnidentifiedRequestDTO(

    @Schema(description = "Data estimada.", example = "97")
    @NotNull(message = "{validation.assigned.value.cannot.be.null}")
    @Positive(message = "{validation.required.positive.integer}")
    int estimatedAge,

    @Schema(description = "Gênero (MALE / FEMALE).", example = "MALE")
    @NotNull(message = "{validation.assigned.value.cannot.be.null}")
    GenderType gender,

    @Schema(description = "Cor da pele (WHITE / BLACK / BROWN / YELLOW / INDIGENOUS).", example = "WHITE")
    @NotNull(message = "{validation.assigned.value.cannot.be.null}")
    SkinColor skinColor,

    @Schema(description = "Altura medida em metros.", example = "1.84")
    @NotNull(message = "{validation.assigned.value.cannot.be.null}")
    @Positive(message = "{validation.required.positive}")
    float stature,

    @Schema(description = "Cor do cabelo.", example = "Branco")
    @NotBlank(message = "{validation.text.cannot.be.blank.or.null}")
    String hairColor,

    @Schema(description = "Tipo do cabelo (STRAIGHT / WAVY / CURLY / KINKY).", example = "STRAIGHT")
    @NotNull(message = "{validation.assigned.value.cannot.be.null}")
    HairType hairType,
    
    @Schema(description = "Cor dos olhos.", example = "Verdes")
    @NotBlank(message = "{validation.text.cannot.be.blank.or.null}")
    String eyeColor,

    @Schema(description = "Tipo dos olhos (ALMOND_SHAPED / ROUND / DROOPY / DEEP_SET / PROTRUDING / ASIAN / ASYMMETRICAL).", example = "ROUND")
    @NotNull(message = "{validation.assigned.value.cannot.be.null}")
    EyeType eyeType,

    @Schema(description = "ID do declarante.", example = "1")
    @NotNull(message = "{validation.assigned.value.cannot.be.null}")
    @Positive(message = "{validation.required.positive}")
    DeclarantEntity declarantId
) {

}
