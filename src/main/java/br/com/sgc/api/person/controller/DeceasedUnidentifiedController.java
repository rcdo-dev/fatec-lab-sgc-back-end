package br.com.sgc.api.person.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.sgc.api.person.dto.request.DeceasedUnidentifiedRequestDTO;
import br.com.sgc.api.person.dto.response.DeceasedUnidentifiedResponseDTO;
import br.com.sgc.api.person.service.DeceasedUnidentifiedService;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/deceased-unidentified")
@Tag(name = "Falecido Não identificado", description = "Operações relacionadas ao cadastro falecido não identificado.")
public class DeceasedUnidentifiedController {
    private final DeceasedUnidentifiedService deceasedUnidentifiedService;

    @GetMapping
    public ResponseEntity<DeceasedUnidentifiedResponseDTO> create(
            @Valid @RequestBody DeceasedUnidentifiedRequestDTO request) {
        var response = deceasedUnidentifiedService.save(request);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }
}
