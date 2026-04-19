package br.com.sgc.api.person.dto.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import br.com.sgc.api.common.enums.DeceasedType;
import br.com.sgc.api.common.enums.genderType;

import br.com.sgc.api.person.dto.response.support.DocumentInfoResponseDTO;
import br.com.sgc.api.person.entity.DeclarantEntity;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Deceased Response", description = "DTO de resposta para o falecido.")
@JsonPropertyOrder({"id", "name", "deceasedType", "birthDate", "gender", "document", "occupation", "fathersName", "mothersName", "naturalness", "cityResident", "observations", "declarantId"})
public record DeceasedResponseDTO(
    @Schema(description = "Identificador único do falecido.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    Long id,

    @Schema(description = "Nome do falecido.", example = "Alexandre Magno Abrão.")
    String name,

    @Schema(description = "Tipo do falecido (HUMAN / PET).", example = "HUMAN")
    DeceasedType deceasedType,

    @Schema(description = "Data de nascimento.", example = "1970-04-09")
    LocalDate birthDate,

    @Schema(description = "Gênero (MAN / WOMAN).", example = "MAN")
    genderType gender,

    @Schema(description = "Documentos de identificação do falecido.")
    DocumentInfoResponseDTO document,

    @Schema(description = "Profissão.", example = "Cantor-compositor")
    String occupation,

    @Schema(description = "Nome do pai", example = "Geraldo Abrão de Jesus")
    String fathersName,

    @Schema(description = "Nome do mãe", example = "Nilda Abrão")
    String mothersName,

    @Schema(description = "Local de nascimento.", example = "São Paulo")
    String naturalness,

    @Schema(description = "É morador da cidade?", example = "false")
    boolean cityResident,

    @Schema(description = "Campo para anotar observações.", example = "Faleceu cedo demais.")
    String observations,

    @Schema(description = "ID do declarante.", example = "1")
    DeclarantEntity declarantId
) {

}
