package br.com.sgc.api.person.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DocumentInfoRequestDTO(

    @Schema(description = "RG do falecido.", example = "40.456.789-0")
    @NotBlank(message = "{validation.deceased.rg.required}")
    String rg,

    @Schema(description = "CPF do falecido.", example = "741.258.968-12")
    @NotBlank(message = "{validation.deceased.cpf.required}")
    @Pattern(regexp = "\\d{11}", message = "{validation.deceased.cpf.invalid}")
    String cpf
) {

}
