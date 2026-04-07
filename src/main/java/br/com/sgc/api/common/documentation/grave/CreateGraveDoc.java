package br.com.sgc.api.common.documentation.grave;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.http.MediaType;

import br.com.sgc.api.cemetery.dto.response.GraveResponseDTO;
import br.com.sgc.api.common.exception.ApiErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Operation(summary = "Cadastrar sepultura.")
@ApiResponses(value = {
    @ApiResponse(
        responseCode = "201",
        description = "Sepultura criada com sucesso,",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = GraveResponseDTO.class),
            examples = @ExampleObject(
                name = "Sepultura criada.",
                value = """
                        {
                            "id": 1,
                            "number": 127,
                            "graveType": "EARTH",
                            "bodyCapacity": 2,
                            "areaType": "COMMON",
                            "status": "AVAILABLE",
                            "blocked": false,
                            "reason": null,
                            "blockId": 1,
                            "active": true
                        }
                        """
            )
        )
    ),
    @ApiResponse(
        responseCode = "400",
        description = "Dados inválidos.",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ApiErrorResponse.class),
            examples = @ExampleObject(
                name = "Erro de validação.",
                value = """
                        {
                            "timestamp": "2026-03-26T10:15:30",
                            "status": 400,
                            "error": "Invalid field",
                            "message": "Campo com dados inválidos.",
                            "path": "/api/graves"
                        }
                        """
            )
        )
    ),
    @ApiResponse(
        responseCode = "409",
        description = "Já existe uma sepultura com esse nome.",
        content = @Content(
            mediaType = MediaType.APPLICATION_JSON_VALUE,
            schema = @Schema(implementation = ApiErrorResponse.class),
            examples = @ExampleObject(
                name = "Conflito de negócio.",
                value = """
                        {
                            "timestamp": "2026-03-26T10:15:30",
                            "status": 409,
                            "error": "Business rule violation",
                            "message": "Já existe uma quadra cadastrada com esse número.",
                            "path": "/api/graves"
                        }
                        """
            )
        )
    )
})
public @interface CreateGraveDoc {

}
