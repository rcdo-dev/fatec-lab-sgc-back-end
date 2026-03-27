package br.com.sgc.api.common.documentation.cemetery;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.http.MediaType;

import br.com.sgc.api.cemetery.dto.response.CemeteryResponseDTO;
import br.com.sgc.api.common.exception.ApiErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Inativar cemitério")
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "200",
        description = "Cemitério inativado com sucesso.",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = CemeteryResponseDTO.class)
        )
    ),
    @ApiResponse(
        responseCode = "404",
        description = "Cemitério não encontrado.",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ApiErrorResponse.class),
            examples = @ExampleObject(
                name = "Recurso não encontrado.",
                value = """
                        {
                            "timestamp": "2026-03-26T10:15:30",
                            "status": 404,
                            "error": "Resource not found.",
                            "message": "O recurso solicitado não existe/não foi encontrado.",
                            "path": "/api/cemetery"
                        }
                        """
            )
        )
    )
})
public @interface InactivateCemeteryDoc {

}
