package br.com.sgc.api.cemetery.controller;

import java.net.URI;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.sgc.api.cemetery.dto.request.CemeteryRequestDTO;
import br.com.sgc.api.cemetery.dto.response.CemeteryResponseDTO;
import br.com.sgc.api.cemetery.service.CemeteryService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cemetery")
public class CemeteryController {
    private final CemeteryService cemeteryService;

    @PostMapping
    public ResponseEntity<CemeteryResponseDTO> create(@Valid @RequestBody CemeteryRequestDTO cemeteryReq) {

        var response = cemeteryService.save(cemeteryReq);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

}
