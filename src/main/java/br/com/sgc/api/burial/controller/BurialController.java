package br.com.sgc.api.burial.controller;

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

import br.com.sgc.api.burial.dto.request.BurialRequestDTO;
import br.com.sgc.api.burial.dto.response.BurialResponseDTO;
import br.com.sgc.api.burial.service.BurialService;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/burials")
@Tag(name = "Sepultamento", description = "Operações relacionadas ao sepultamento.")
public class BurialController {

    private final BurialService burialService;

    @PostMapping
    public ResponseEntity<BurialResponseDTO> create(@Valid @RequestBody BurialRequestDTO request) {
        var response = burialService.save(request);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<BurialResponseDTO>> findAll() {
        return ResponseEntity.ok(burialService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BurialResponseDTO> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(burialService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BurialResponseDTO> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody BurialRequestDTO request) {
        return ResponseEntity.ok(burialService.update(id, request));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<BurialResponseDTO> cancel(@PathVariable("id") Long id) {
        return ResponseEntity.ok(burialService.cancel(id));
    }
}
