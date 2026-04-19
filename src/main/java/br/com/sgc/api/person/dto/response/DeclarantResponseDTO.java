package br.com.sgc.api.person.dto.response;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import br.com.sgc.api.person.dto.response.support.AddressInfoResponseDTO;
import br.com.sgc.api.person.dto.response.support.ContactInfoResponseDTO;
import br.com.sgc.api.person.dto.response.support.DocumentInfoResponseDTO;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Declarant Response", description = "DTO de resposta para o declarante.")
@JsonPropertyOrder({"id", "name", "document", "contact", "address", "occupation"})
public record DeclarantResponseDTO(
    @Schema(description = "Identificador único do falecido.", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    Long id,

    @Schema(description = "Nome do declarante", example = "Cleber Atala.")
    String name,

    @Schema(description = "Documentos de identificação do declarante.")
    DocumentInfoResponseDTO document,

    @Schema(description = "Contatos do declarante.")
    ContactInfoResponseDTO contact,

    @Schema(description = "Endereço do declarante.")
    AddressInfoResponseDTO address,

    @Schema(description = "Profissão do declarante.")
    String occupation
) {

}
