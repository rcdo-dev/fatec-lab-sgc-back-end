package br.com.sgc.api.cemetery.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record BlockRequestDTO(
                @Positive int number,
                String description,
                boolean active,
                @NotNull @Positive Long cemeteryId) {
}
