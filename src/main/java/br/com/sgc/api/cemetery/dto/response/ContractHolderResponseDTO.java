package br.com.sgc.api.cemetery.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Contract Holder Response", description = "DTO de resposta para titular de contrato.")
public record ContractHolderResponseDTO(
        @Schema(description = "Identificador unico do titular.", example = "1")
        Long id,

        @Schema(description = "Nome do titular.", example = "Lucas Pedro Honorato")
        String name,

        @Schema(description = "CPF do titular.", example = "74125896812")
        String cpf,

        @Schema(description = "Telefone do titular.", example = "11996582541")
        String phone,

        @Schema(description = "E-mail do titular.", example = "titular@email.com")
        String email,

        @Schema(description = "Indica se o titular esta ativo.", example = "true")
        boolean active
) {

}
