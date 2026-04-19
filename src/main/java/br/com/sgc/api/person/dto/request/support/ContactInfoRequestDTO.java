package br.com.sgc.api.person.dto.request.support;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record ContactInfoRequestDTO(

    @Schema(description = "Número de telefone.", example = "11996582541")
    @NotBlank(message = "{validation.contact.phone.required}")
    String phone,

    @Schema(description = "Contato de e-mail.", example = "meuContato@provedor.com")
    @NotBlank(message = "{validation.contact.email.required}")
    String email
) {

}
