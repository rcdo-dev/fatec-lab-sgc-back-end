package br.com.sgc.api.person.dto.request;

import java.time.LocalDate;
import java.time.LocalTime;

import br.com.sgc.api.person.entity.DeceasedEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;

@Schema(title = "Death Request", description = "DTO de entrada para cadastro do óbito.")
public record DeathRequestDTO(
    @Schema(description = "Local do óbito.", example = "Apartamento")
    @NotBlank(message = "{validation.text.cannot.be.blank.or.null}")
    String place,

    @Schema(description = "Data do óbito.", example = "2013-03-06")
    @NotNull(message = "{validation.assigned.value.cannot.be.null}")
    @PastOrPresent(message = "{validation.date.cannot.be.in.the.future}")
    LocalDate date,

    @Schema(description = "Hora do óbito", example = "05:30")
    @NotNull(message = "{validation.assigned.value.cannot.be.null}")
    LocalTime time,

    @Schema(description = "Causa da morte", example = "Overdose.")
    @NotBlank(message = "{validation.text.cannot.be.blank.or.null}")
    String causeDeath,

    @Schema(description = "Nome do médico responsável.", example = "Dr. Samu")
    @NotBlank(message = "{validation.text.cannot.be.blank.or.null}")
    String doctorResponsible,

    @Schema(description = "Número da certidão de óbito.", example = "01234 / Não consta")
    @NotBlank(message = "{validation.text.cannot.be.blank.or.null}")
    String certificateNumber,

    @Schema(description = "Campo para observações do óbito", example = "Poderia estar vivo e cantando.")
    String observationsString,

    @Schema(description = "Indica a qual falecido este óbito pertence.", example = "1")
    @Positive(message = "{validation.required.positive.integer}")
    DeceasedEntity deceasedId
) {

}
