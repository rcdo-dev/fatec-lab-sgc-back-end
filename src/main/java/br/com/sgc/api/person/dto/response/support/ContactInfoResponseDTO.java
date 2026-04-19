package br.com.sgc.api.person.dto.response.support;

import io.swagger.v3.oas.annotations.media.Schema;

public record ContactInfoResponseDTO(
    @Schema(description = "Número de telefone.", example = "11996582541")
    String phone,

    @Schema(description = "Contato de e-mail.", example = "meuContato@provedor.com")
    String email
) {

}
