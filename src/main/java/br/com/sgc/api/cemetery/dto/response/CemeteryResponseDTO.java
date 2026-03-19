package br.com.sgc.api.cemetery.dto.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({ "id", "name", "foundation", "active" })
public record CemeteryResponseDTO(
        Long id,
        String name,
        LocalDate foundation,
        boolean active) {
}
