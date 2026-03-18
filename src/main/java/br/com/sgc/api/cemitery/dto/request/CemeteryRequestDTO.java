package br.com.sgc.api.cemitery.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CemeteryRequestDTO {

    @NotBlank
    private String name;

    @NotNull
    private LocalDate fundation;

    @NotNull
    private Boolean active;
}
