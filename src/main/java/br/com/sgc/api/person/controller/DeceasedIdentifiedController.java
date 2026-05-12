package br.com.sgc.api.person.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.sgc.api.person.dto.request.DeceasedIdentifiedRequestDTO;
import br.com.sgc.api.person.dto.response.DeceasedIdentifiedResponseDTO;
import br.com.sgc.api.person.service.DeceasedIdentifiedService;

import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/deceased-identified")
@Tag(name = "Falecido Identificado", description = "Operações relacionadas ao cadastro falecido identificado.")
public class DeceasedIdentifiedController {
    private final DeceasedIdentifiedService deceasedIdentifiedService;

    @PostMapping
    public ResponseEntity<DeceasedIdentifiedResponseDTO> create(
            @Valid @RequestBody DeceasedIdentifiedRequestDTO request) {
        var response = deceasedIdentifiedService.save(request);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<DeceasedIdentifiedResponseDTO>> findAll() {
        return ResponseEntity.ok(deceasedIdentifiedService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeceasedIdentifiedResponseDTO> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(deceasedIdentifiedService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeceasedIdentifiedResponseDTO> update(@PathVariable("id") Long id,
            @Valid @RequestBody DeceasedIdentifiedRequestDTO request) {
        return ResponseEntity.ok(deceasedIdentifiedService.update(id, request));
    }

    @PatchMapping("/{id}/archive")
    public ResponseEntity<DeceasedIdentifiedResponseDTO> archive(@PathVariable("id") Long id) {
        deceasedIdentifiedService.archive(id);
        return ResponseEntity.noContent().build();
    }
}
