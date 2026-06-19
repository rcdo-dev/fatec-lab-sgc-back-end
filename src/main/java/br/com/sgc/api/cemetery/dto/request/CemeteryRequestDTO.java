package br.com.sgc.api.cemetery.dto.request;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(title = "Cemetery Request", description = "DTO de entrada para cadastro de cemitério.")
public record CemeteryRequestDTO(
        @Schema(description = "Nome do cemitério.", example = "Cemitério do Cambiri")
        @NotBlank(message = "{validation.cemetery.name.required}")
        String name,

        @Schema(description = "Data de fundação do cemitério.", example = "1983-01-01")
        @NotNull(message = "{validation.assigned.value.cannot.be.null}")
        @PastOrPresent(message = "{validation.date.cannot.be.in.the.future}")
        LocalDate foundation,

        @Schema(description = "Indica se o cemitério está ativo.", example = "true")
        boolean active,

        @Schema(description = "Duração máxima do velório, em minutos.", example = "180")
        @Positive(message = "{validation.required.positive}")
        Integer wakeDurationMinutes,

        @Schema(description = "Indica se o cemitério cobra velório.", example = "false")
        Boolean wakeCharged,

        @Schema(description = "Valor cobrado pelo velório.", example = "150.00")
        @PositiveOrZero(message = "{validation.required.positive.or.zero}")
        BigDecimal wakeFee) {

    public CemeteryRequestDTO(String name, LocalDate foundation, boolean active) {
        this(name, foundation, active, null, null, null);
    }
}
