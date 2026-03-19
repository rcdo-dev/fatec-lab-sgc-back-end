package br.com.sgc.api.cemetery.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

public record CemeteryRequestDTO(
        @NotBlank String name,
        @NotNull @PastOrPresent LocalDate foundation,
        boolean active) {
}
