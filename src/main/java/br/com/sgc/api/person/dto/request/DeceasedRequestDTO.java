package br.com.sgc.api.person.dto.request;

import java.time.LocalDate;

import br.com.sgc.api.common.enums.DeceasedType;
import br.com.sgc.api.common.enums.genderType;
import br.com.sgc.api.person.entity.DeclarantEntity;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Deceased Request", description = "DTO de entrada para persistência de dados do falecido.")
public record DeceasedRequestDTO(
    @Schema(description = "Nome do falecido.", example = "Alexandre Magno Abrão.")
    @NotBlank(message = "{validation.text.cannot.be.blank.or.null}")
    String name,

    @Schema(description = "Tipo do falecido (HUMAN / PET).", example = "HUMAN")
    @NotNull(message = "{validation.assigned.value.cannot.be.null}")
    DeceasedType deceasedType,

    @Schema(description = "Data de nascimento.", example = "1970-04-09")
    @NotNull(message = "{validation.assigned.value.cannot.be.null}")
    @PastOrPresent(message = "{validation.date.cannot.be.in.the.future}")
    LocalDate birthDate,

    @Schema(description = "Gênero (MAN / WOMAN).", example = "MAN")
    @NotNull(message = "{validation.assigned.value.cannot.be.null}")
    genderType gender,

    @NotNull(message = "{validation.deceased.document.required}")
    @Valid
    DocumentInfoRequestDTO document,

    @Schema(description = "Profissão.", example = "Cantor-compositor")
    @NotBlank(message = "{validation.text.cannot.be.blank.or.null}")
    String occupation,

    @Schema(description = "Nome do pai", example = "Geraldo Abrão de Jesus")
    @NotBlank(message = "{validation.text.cannot.be.blank.or.null}")
    String fathersName,

    @Schema(description = "Nome do mãe", example = "Nilda Abrão")
    @NotBlank(message = "{validation.text.cannot.be.blank.or.null}")
    String mothersName,

    @Schema(description = "Local de nascimento.", example = "São Paulo")
    @NotBlank(message = "{validation.text.cannot.be.blank.or.null}")
    String naturalness,

    @Schema(description = "É morador da cidade?", example = "false")
    @NotNull(message = "{validation.assigned.value.cannot.be.null}")
    boolean cityResident,

    @Schema(description = "Campo para anotar observações.", example = "Faleceu cedo demais.")
    String observations,

    @Schema(description = "ID do declarante.", example = "1")
    @NotNull(message = "{validation.assigned.value.cannot.be.null}")
    @Positive(message = "{validation.required.positive.integer}")
    DeclarantEntity declarantId
) {

}
