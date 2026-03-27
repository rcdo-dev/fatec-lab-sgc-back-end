package br.com.sgc.api.cemetery.controller;

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

import br.com.sgc.api.cemetery.dto.request.GraveRequestDTO;
import br.com.sgc.api.cemetery.dto.request.GraveUpdateRequestDTO;
import br.com.sgc.api.cemetery.dto.response.GraveResponseDTO;

import br.com.sgc.api.cemetery.service.GraveService;

import br.com.sgc.api.common.documentation.grave.CreateGraveDoc;
import br.com.sgc.api.common.documentation.grave.FindAllGraveDoc;
import br.com.sgc.api.common.documentation.grave.FindGraveByIdDoc;
import br.com.sgc.api.common.documentation.grave.InactivateGraveDoc;
import br.com.sgc.api.common.documentation.grave.UpdateGraveDoc;

import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/graves")
@Tag(name = "Sepulturas", description = "Operações relacionadas ao cadastro de sepulturas.")
public class GraveController {
    private final GraveService graveService;

    @CreateGraveDoc
    @PostMapping
    public ResponseEntity<GraveResponseDTO> create(@Valid @RequestBody GraveRequestDTO request) {
        var response = graveService.save(request);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @FindAllGraveDoc
    @GetMapping
    public ResponseEntity<List<GraveResponseDTO>> findAll() {
        return ResponseEntity.ok(graveService.findAll());
    }

    @FindGraveByIdDoc
    @GetMapping("/{id}")
    public ResponseEntity<GraveResponseDTO> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(graveService.findById(id));
    }

    @UpdateGraveDoc
    @PutMapping("/{id}")
    public ResponseEntity<GraveResponseDTO> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody GraveUpdateRequestDTO request) {
        return ResponseEntity.ok(graveService.update(id, request));
    }

    @InactivateGraveDoc
    @PatchMapping("/{id}/inactive")
    public ResponseEntity<GraveResponseDTO> inactivate(@PathVariable("id") Long id) {
        return ResponseEntity.ok(graveService.inactivate(id));
    }

}
