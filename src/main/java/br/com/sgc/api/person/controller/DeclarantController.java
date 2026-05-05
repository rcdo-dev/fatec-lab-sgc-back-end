package br.com.sgc.api.person.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.sgc.api.person.dto.request.DeclarantRequestDTO;
import br.com.sgc.api.person.dto.response.DeclarantResponseDTO;
import br.com.sgc.api.person.service.DeclarantService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/declarant")
@Tag(name = "Declarante", description = "Operações relacionadas ao cadastro do declarante.")
public class DeclarantController {
    private final DeclarantService declarantService;

    @PostMapping
    public ResponseEntity<DeclarantResponseDTO> create(@Valid @RequestBody DeclarantRequestDTO request) {
        var response = declarantService.save(request);

        // Travar declarantes repetidos.
        
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

}
