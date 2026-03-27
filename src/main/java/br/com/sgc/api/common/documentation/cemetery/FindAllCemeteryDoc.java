package br.com.sgc.api.common.documentation.cemetery;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.http.MediaType;

import br.com.sgc.api.cemetery.dto.response.CemeteryResponseDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Buscar todos os cemitérios.")
@ApiResponse(
    responseCode = "200",
    description = "Lista de cemitérios retornada com sucesso.",
    content = @Content(
        mediaType = MediaType.APPLICATION_JSON_VALUE,
        array = @ArraySchema(
            schema = @Schema(
                implementation = CemeteryResponseDTO.class
            )
        )
    )
)
public @interface FindAllCemeteryDoc {

}
