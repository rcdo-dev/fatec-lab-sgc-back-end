package br.com.sgc.api.person.dto.request;

import br.com.sgc.api.person.dto.request.support.AddressInfoRequestDTO;
import br.com.sgc.api.person.dto.request.support.ContactInfoRequestDTO;
import br.com.sgc.api.person.dto.request.support.DocumentInfoRequestDTO;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Declarant Request", description = "DTO de entrada para cadastro do declarante.")
public record DeclarantRequestDTO(
    @Schema(description = "Nome do declarante", example = "Cleber Atala.")
    @NotBlank(message = "{validation.text.cannot.be.blank.or.null}")
    String name,

    @Schema(description = "Documentos de identificação do declarante.")
    @NotNull(message = "{validation.deceased.document.required}")
    @Valid
    DocumentInfoRequestDTO document,

    @Schema(description = "Contatos do declarante.")
    @NotNull(message = "{validation.deceased.document.required}")
    @Valid
    ContactInfoRequestDTO contact,

    @Schema(description = "Endereço do declarante.")
    @NotNull(message = "{validation.deceased.document.required}")
    @Valid
    AddressInfoRequestDTO adress,

    @Schema(description = "Profissão do declarante.")
    @NotBlank(message = "{validation.text.cannot.be.blank.or.null}")
    String occupation
) {

}
