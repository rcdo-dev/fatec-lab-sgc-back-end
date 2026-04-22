package br.com.sgc.api.person.dto.response;

import java.time.LocalDate;
import java.time.LocalTime;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Death Response", description = "DTO de resposta para o óbito.")
@JsonPropertyOrder({"id", "place", "date", "time", "causeDeath", "doctorResponsible", "certificateNumber", "observations", "deceasedId"})
public record DeathResponseDTO(
    @Schema(description = "Identificador único do falecido.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    Long id,

    @Schema(description = "Local do óbito.", example = "Apartamento")
    String place,

    @Schema(description = "Data do óbito.", example = "2013-03-06")
    LocalDate date,

    @Schema(description = "Hora do óbito", example = "05:30")
    LocalTime time,

    @Schema(description = "Causa da morte", example = "Overdose.")
    String causeDeath,

    @Schema(description = "Nome do médico responsável.", example = "Dr. Samu")
    String doctorResponsible,

    @Schema(description = "Número da certidão de óbito.", example = "01234 / Não consta")
    String certificateNumber,

    @Schema(description = "Campo para observações do óbito", example = "Poderia estar vivo e cantando.")
    String observations,

    @Schema(description = "Indica a qual falecido este óbito pertence.", example = "1")
    Long deceasedId
) {

}
