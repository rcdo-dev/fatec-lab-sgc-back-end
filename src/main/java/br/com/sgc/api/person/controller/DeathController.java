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

import br.com.sgc.api.person.dto.request.DeathRequestDTO;
import br.com.sgc.api.person.dto.response.DeathResponseDTO;
import br.com.sgc.api.person.service.DeathService;

import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/death")
@Tag(name = "Óbito", description = "Operações relacionadas ao óbito do falecido.")
public class DeathController {

    private final DeathService deathService;

    @PostMapping
    public ResponseEntity<DeathResponseDTO> create(@Valid @RequestBody DeathRequestDTO request) {
        var response = deathService.save(request);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.id()).toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<DeathResponseDTO>> listAll() {
        return ResponseEntity.ok(deathService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<DeathResponseDTO> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(deathService.findById(id));
    }

    @PutMapping
    public ResponseEntity<DeathResponseDTO> update(@PathVariable("id") Long id,
            @Valid @RequestBody DeathRequestDTO request) {
        return ResponseEntity.ok(deathService.update(id, request));
    }

    @DeleteMapping
    public ResponseEntity<DeathResponseDTO> delete(@PathVariable("id") Long id) {
        deathService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
