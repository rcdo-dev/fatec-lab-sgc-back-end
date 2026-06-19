package br.com.sgc.api.burial.controller;

import java.net.URI;
import java.util.List;

import br.com.sgc.api.burial.dto.request.WakeRequestDTO;
import br.com.sgc.api.burial.dto.response.WakeResponseDTO;
import br.com.sgc.api.burial.service.WakeService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/wakes")
@Tag(name = "Velório", description = "Operações relacionadas ao agendamento de velório.")
public class WakeController {

    private final WakeService wakeService;

    @PostMapping
    public ResponseEntity<WakeResponseDTO> create(@Valid @RequestBody WakeRequestDTO request) {
        var response = wakeService.save(request);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<WakeResponseDTO>> findAll() {
        return ResponseEntity.ok(wakeService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<WakeResponseDTO> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(wakeService.findById(id));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<WakeResponseDTO> cancel(@PathVariable("id") Long id) {
        return ResponseEntity.ok(wakeService.cancel(id));
    }
}
