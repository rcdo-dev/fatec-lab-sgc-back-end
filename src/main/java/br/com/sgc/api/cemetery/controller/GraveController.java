package br.com.sgc.api.cemetery.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.sgc.api.cemetery.dto.request.GraveRequestDTO;
import br.com.sgc.api.cemetery.dto.response.GraveResponseDTO;
import br.com.sgc.api.cemetery.service.GraveService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/graves")
public class GraveController {
    private final GraveService graveService;

    @PostMapping
    public ResponseEntity<GraveResponseDTO> create(@Valid @RequestBody GraveRequestDTO request) {
        var response = graveService.save(request);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

}
