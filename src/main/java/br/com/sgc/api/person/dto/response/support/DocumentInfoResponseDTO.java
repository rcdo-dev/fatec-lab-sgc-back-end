package br.com.sgc.api.person.dto.response.support;

import io.swagger.v3.oas.annotations.media.Schema;

public record DocumentInfoResponseDTO(
    @Schema(description = "Número do RG.", example = "404567890")
    String rg,

    @Schema(description = "Número do CPF com 11 dígitos.", example = "74125896812")
    String cpf
) {

}
