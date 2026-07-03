package br.com.sgc.api.cemetery.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@Schema(title = "Contract Holder Request", description = "DTO de entrada para titular de contrato.")
public record ContractHolderRequestDTO(
        @Schema(description = "Nome do titular.", example = "Lucas Pedro Honorato")
        @NotBlank(message = "{validation.text.cannot.be.blank.or.null}")
        String name,

        @Schema(description = "CPF do titular com 11 digitos.", example = "74125896812")
        @NotBlank(message = "{validation.document.cpf.required}")
        @Pattern(regexp = "\\d{11}", message = "{validation.document.cpf.invalid}")
        String cpf,

        @Schema(description = "Telefone do titular.", example = "11996582541")
        @NotBlank(message = "{validation.contact.phone.required}")
        String phone,

        @Schema(description = "E-mail do titular.", example = "titular@email.com")
        @NotBlank(message = "{validation.contact.email.required}")
        String email
) {

}
