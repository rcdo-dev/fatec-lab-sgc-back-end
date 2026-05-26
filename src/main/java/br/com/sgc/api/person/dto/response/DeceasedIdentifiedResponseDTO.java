package br.com.sgc.api.person.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import br.com.sgc.api.common.enums.DeceasedStatus;
import br.com.sgc.api.common.enums.GenderIdentityType;
import br.com.sgc.api.common.enums.GenderType;

import br.com.sgc.api.person.dto.response.support.DocumentInfoResponseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Deceased Identified Response", description = "DTO de resposta para o falecido identificado.")
@JsonPropertyOrder({"id", "name", "deceasedType", "birthDate", "gender", "document", "occupation", "fathersName", "mothersName", "naturalness", "cityResident", "observations", "declarantId"})
public record DeceasedIdentifiedResponseDTO(
    @Schema(description = "Identificador único do falecido.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    Long id,

    @Schema(description = "Nome do falecido.", example = "Alexandre Magno Abrão.")
    String name,

    @Schema(description = "Data de nascimento.", example = "1970-04-09")
    LocalDate birthDate,

    @Schema(description = "Gênero (MAN / WOMAN).", example = "MAN")
    GenderType gender,

    @Schema(description = "Gênero (CISGENDER / TRANSGENDER / NON_BINARY).", example = "TRANSGENDER")
    GenderIdentityType genderIdentity,

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
