package br.com.sgc.api.person.controller;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.sgc.api.person.dto.request.DeclarantRequestDTO;
import br.com.sgc.api.person.dto.response.DeclarantResponseDTO;
import br.com.sgc.api.person.service.DeclarantService;

import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

// Incluir os exemplos de código de exceção no swagger.

@RestController
@RequiredArgsConstructor
@RequestMapping("/declarant")
@Tag(name = "Declarante", description = "Operações relacionadas ao cadastro do declarante.")
public class DeclarantController {
    private final DeclarantService declarantService;

    @PostMapping
    public ResponseEntity<DeclarantResponseDTO> create(@Valid @RequestBody DeclarantRequestDTO request) {
        var response = declarantService.save(request);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<DeclarantResponseDTO>> findAll() {
        return ResponseEntity.ok(declarantService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeclarantResponseDTO> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(declarantService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<DeclarantResponseDTO> update(@PathVariable("id") Long id,
            @Valid @RequestBody DeclarantRequestDTO request) {
        return ResponseEntity.ok(declarantService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeclarantResponseDTO> delete(@PathVariable("id") Long id) {
        declarantService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
