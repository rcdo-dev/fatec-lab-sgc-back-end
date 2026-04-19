package br.com.sgc.api.person.dto.response.support;

import io.swagger.v3.oas.annotations.media.Schema;

public record AddressInfoResponseDTO(
    @Schema(description = "Nome da rua.", example = "Avenida Presidente Wilson")
    String street,

    @Schema(description = "Número da rua.", example = "S/N ou 439")
    String number,

    @Schema(description = "Bairro.", example = "José Menino.")
    String district,

    @Schema(description = "Cidade.", example = "Santos")
    String city,

    @Schema(description = "Estado.", example = "São Paulo")
    String state,

    @Schema(description = "CEP.", example = "11065-201")
    String cep
) {

}
