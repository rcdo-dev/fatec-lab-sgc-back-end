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

import br.com.sgc.api.cemetery.dto.request.CemeteryRequestDTO;
import br.com.sgc.api.cemetery.dto.response.CemeteryResponseDTO;
import br.com.sgc.api.cemetery.service.CemeteryService;
import br.com.sgc.api.common.documentation.cemetery.CreateCemeteryDoc;
import br.com.sgc.api.common.documentation.cemetery.FindAllCemeteryDoc;
import br.com.sgc.api.common.documentation.cemetery.FindCemeteryByIdDoc;
import br.com.sgc.api.common.documentation.cemetery.InactivateCemeteryDoc;
import br.com.sgc.api.common.documentation.cemetery.UpdateCemeteryDoc;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cemeteries")
@Tag(name = "Cemitérios", description = "Operações relacionadas ao cadastro de cemitérios.")
public class CemeteryController {
    private final CemeteryService cemeteryService;

    @CreateCemeteryDoc
    @PostMapping
    public ResponseEntity<CemeteryResponseDTO> create(@Valid @RequestBody CemeteryRequestDTO request) {

        var response = cemeteryService.save(request);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @FindAllCemeteryDoc
    @GetMapping
    public ResponseEntity<List<CemeteryResponseDTO>> findAll() {
        return ResponseEntity.ok(cemeteryService.findAll());
    }

    @FindCemeteryByIdDoc
    @GetMapping("/{id}")
    public ResponseEntity<CemeteryResponseDTO> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(cemeteryService.findById(id));
    }

    @UpdateCemeteryDoc
    @PutMapping("/{id}")
    public ResponseEntity<CemeteryResponseDTO> update(@PathVariable("id") Long id,
            @Valid @RequestBody CemeteryRequestDTO request) {
        return ResponseEntity.ok(cemeteryService.update(id, request));
    }

    @InactivateCemeteryDoc
    @PatchMapping("/{id}/inactive")
    public ResponseEntity<CemeteryResponseDTO> inactivate(@PathVariable Long id) {
        return ResponseEntity.ok(cemeteryService.inactivate(id));
    }
}
