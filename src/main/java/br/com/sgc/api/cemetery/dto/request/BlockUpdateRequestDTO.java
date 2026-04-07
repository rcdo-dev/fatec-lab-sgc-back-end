package br.com.sgc.api.cemetery.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Block Request Update", description = "DTO de entrada para atualização da descrição da quadra.")
public record BlockUpdateRequestDTO(

                @Schema(description = "Descrição da quadra.", example = "Setor oeste")
                String description) {

}
