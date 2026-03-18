package br.com.sgc.api.cemitery.dto.response;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;

@Data
@JsonPropertyOrder({ "id", "name", "fundation", "active" })
public class CemeteryResponseDTO {
    private Long id;
    private String name;
    private LocalDate fundation;
    private Boolean active;
}
