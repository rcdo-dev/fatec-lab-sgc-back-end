package br.com.sgc.api.person.dto.request.support;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record AddressInfoRequestDTO(

    @Schema(description = "Nome da rua.", example = "Avenida Presidente Wilson")
    @NotBlank(message = "{validation.address.street.required}")
    String street,

    @Schema(description = "Número da rua.", example = "S/N ou 439")
    @NotBlank(message = "{validation.address.number.required}")
    String number,

    @Schema(description = "Bairro.", example = "José Menino.")
    @NotBlank(message = "{validation.address.district.required}")
    String district,

    @Schema(description = "Cidade.", example = "Santos")
    @NotBlank(message = "{validation.address.city.required}")
    String city,

    @Schema(description = "Estado.", example = "São Paulo")
    @NotBlank(message = "{validation.address.state.required}")
    String state,

    @Schema(description = "CEP.", example = "11065-201")
    @NotBlank(message = "{validation.address.cep.required}")
    String cep
) {

}
