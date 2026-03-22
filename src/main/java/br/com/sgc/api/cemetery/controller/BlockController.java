package br.com.sgc.api.cemetery.controller;

import java.net.URI;
import java.util.List;

import org.apache.catalina.connector.Response;
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

import br.com.sgc.api.cemetery.dto.request.BlockRequestDTO;
import br.com.sgc.api.cemetery.dto.response.BlockResponseDTO;
import br.com.sgc.api.cemetery.service.BlockService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/block")
public class BlockController {

    private final BlockService blockService;

    @PostMapping
    public ResponseEntity<BlockResponseDTO> create(@Valid @RequestBody BlockRequestDTO request) {

        var response = blockService.save(request);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(response.id())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    @GetMapping
    public ResponseEntity<List<BlockResponseDTO>> findAll() {
        return ResponseEntity.ok(blockService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BlockResponseDTO> findById(@PathVariable("id") Long id) {
        return ResponseEntity.ok(blockService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<BlockResponseDTO> update(
            @PathVariable("id") Long id,
            @Valid @RequestBody BlockRequestDTO request) {
        return ResponseEntity.ok(blockService.update(id, request));
    }

    @PatchMapping("/{id}/inactive")
    public ResponseEntity<BlockResponseDTO> inactivate(@PathVariable Long id) {
        return ResponseEntity.ok(blockService.inactivate(id));
    }

}
