package br.com.sgc.api.person.dto.request.support;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record DocumentInfoRequestDTO(

    @Schema(description = "Número do RG.", example = "404567890")
    @NotBlank(message = "{validation.document.rg.required}")
    String rg,

    @Schema(description = "Número do CPF com 11 dígitos.", example = "74125896812")
    @NotBlank(message = "{validation.document.cpf.required}")
    @Pattern(regexp = "\\d{11}", message = "{validation.document.cpf.invalid}")
    String cpf
) {

}
